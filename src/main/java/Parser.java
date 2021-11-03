import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Parser{
    private final String url = new String("https://slova.org.ru");
    private final String url_for_site_with_topics = new String("https://www.culture.ru/literature/poems");
    Document page = null;
    Parser(){
        try {
            page = Jsoup.connect(url).get();

        } catch (IOException e) {
            System.out.println("Unable to connect to site");;
        }
    }

    public String findAuthor(String name)
    {
        Elements listAuthors = page.getElementsByClass("grid-321 grid-gap-xl");
            String link_href;
            for (Element element: listAuthors.select("a")) {
                if(element.text().equals(name))
                {
                    link_href = url + element.attr("href");
                    return  link_href;
                }
            }
        return "Не нашлось такого поэта";
    }

    public String showAllAuthorsPoems(String name)
    {
        String allPoems = ""; // for return statement
        String url_for_authors_page = this.findAuthor(name);
        Document new_page = null;
        try{
            new_page = Jsoup.connect(url_for_authors_page).get();
        }catch (IOException e)
        {
            System.out.println("Unable to connect to site");
        }
        Elements listTitlesOfPoems = new_page.getElementsByClass("grid-col-3");
        for (Element element: listTitlesOfPoems.select("a"))
        {
            allPoems = allPoems + element.text() + " \n";
        }
        return allPoems;
    }

    public boolean isAuthor(String name)
    {
        Elements listAuthors = page.getElementsByClass("grid-321 grid-gap-xl");
        String link_href;
        for (Element element: listAuthors.select("a")) {
            if(element.text().equals(name))
            {
                return  true;
            }
        }
        return false;
    }

    public boolean isPoem(String poem_name, String new_url)
    {
        Document new_page = null;
        try{
            new_page = Jsoup.connect(new_url).get();
        }catch (IOException e)
        {
            System.out.println("Unable to connect to site");
        }
        Elements listPoems = new_page.getElementsByClass("container");
        for (Element element: listPoems.select("a")) {
            if(element.text().equals(poem_name)) {
                    return true;
                }
            }
        return false;
    }

    public String findPoem(String poem_name, String full_url)
    {
        Document new_page = null;
        try{
            new_page = Jsoup.connect(full_url).get();
        }catch (IOException e)
        {
            System.out.println("Unable to connect to site");
        }
        Elements listPoems = new_page.getElementsByClass("container");
        String poem;
        for (Element element: listPoems.select("a")) {
            if(element.text().equals(poem_name))
            {
                try {

                    Document page_with_poem = Jsoup.connect(url + element.attr("href")).get();
                    poem = page_with_poem.getElementsByTag("p").text();
                    if(poem.equals(""))
                    {
                        poem = page_with_poem.getElementsByTag("pre").text();
                        return poem;
                    }
                    return poem;
                } catch (IOException e) {
                    System.out.println("Unable to connect to page with poem");
                    return "";
                }
            }
        }
        return "Такого стиха не нашлось";
    }

    public String RandomPoem()
    {

        try {
            page = Jsoup.connect("http://russian-poetry.ru/Random.php").get();

        } catch (IOException e) {
            System.out.println("Unable to connect to site");;
        }
        String poem = page.getElementsByTag("pre").text();
        return poem;
    }



}
