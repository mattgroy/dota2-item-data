package ru.mattgroy.dota2itemdata.scraper

import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.mattgroy.dota2itemdata.item.Item
import java.text.NumberFormat
import java.text.ParseException
import java.util.*


@Component
class WebScraper(
    @Value("\${source.url}") private val url: String,
    @Value("\${source.parse.timeout.ms}") private val timeout: Int,
    @Value("\${source.useragent}") private val userAgent: String
) {

    fun retrieveItemData(itemId: String): Item? {
        var itemData: Item? = null

        try {
            val infoPage = Jsoup
                .connect("$url$itemId/tooltip")
                .userAgent(userAgent)
                .timeout(timeout)
                .get()

            val avatarUrl: String? = infoPage
                .getElementsByClass("avatar").first()
                ?.selectFirst("img")
                ?.attr("abs:src")

            val icon = downloadImage(avatarUrl)

            val name = infoPage
                .getElementsByClass("name").first()
                ?.text()

            var price: Int? = null
            val priceString = infoPage
                .getElementsByClass("price").first()
                ?.text()
            if (!priceString.isNullOrBlank())
                price = try {
                    NumberFormat.getNumberInstance(Locale.ENGLISH).parse(priceString).toInt()
                } catch (parseError: ParseException) {
                    0
                }

            val stats = infoPage
                .getElementsByClass("stat")
                .map { it.text() }

            val descriptionElement = infoPage
                .getElementsByClass("description").first()

            var description: String? = null
            val descriptionBlocks = descriptionElement
                ?.getElementsByClass("description-block")
            if (!descriptionBlocks.isNullOrEmpty())
                description = descriptionBlocks.map { it.text() }.joinToString("\n")

            var cooldown: Int? = null
            val cooldownString = descriptionElement
                ?.getElementsByClass("cooldown")?.first()
                ?.text()
            if (!cooldownString.isNullOrBlank())
                cooldown = NumberFormat.getNumberInstance(Locale.ENGLISH).parse(cooldownString).toInt()

            var manacost: Int? = null
            val manacostString = descriptionElement
                ?.getElementsByClass("manacost")?.first()
                ?.text()
            if (!manacostString.isNullOrBlank())
                manacost = NumberFormat.getNumberInstance(Locale.ENGLISH).parse(manacostString).toInt()

            var buildsInto: List<String>? = null
            val buildsIntoElements = infoPage
                .getElementsByClass("item-builds-into").first()
                ?.getElementsByClass("item")
            if (!buildsIntoElements.isNullOrEmpty())
                buildsInto = buildsIntoElements.mapNotNull { it.selectFirst("a")?.attr("href")?.split("/")?.last() }

            var buildsFrom: List<String>? = null
            val buildsFromElements = infoPage
                .getElementsByClass("item-builds-from").first()
                ?.getElementsByClass("item")
            if (!buildsFromElements.isNullOrEmpty())
                buildsFrom = buildsFromElements.mapNotNull { it.selectFirst("a")?.attr("href")?.split("/")?.last() }

            itemData =
                Item(itemId, name, icon, price, stats, description, cooldown, manacost, buildsInto, buildsFrom)

        } catch (e: HttpStatusException) {
            throw e
        }

        return itemData
    }

    private fun downloadImage(imageUrl: String?): ByteArray? {
        return if (imageUrl.isNullOrBlank()) null
        else Jsoup.connect(imageUrl).ignoreContentType(true).execute().bodyAsBytes();
    }

}
