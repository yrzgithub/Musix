from yt_dlp import *
from bs4 import BeautifulSoup
from requests import get
from youtubesearchpython import *



ydl_opts =  {
    'format': 'm4a/bestaudio',
    "allsubtitles":True,
    'writesubtitles': True,
    'writeautomaticsub': True,
    'subtitlesformat': 'srv1'
}


suggestions = Suggestions(language = "en", region = "IN", timeout = None)



def getUrlsInfo(query,limit=20,lang="en",region="IN",timeout=None):
    search = VideosSearch(query,limit,lang,region,timeout)
    results = search.result()["result"]

    urlInfo = [
        {
            "title" : result["title"],
            "publishedTime":result["publishedTime"],
            "views":result["viewCount"]["short"],
            "thumbnail":result["thumbnails"][0]["url"],
            "channelName":result["channel"]["name"],
            "channelThumnail": result["channel"]["thumbnails"][0]["url"],
            "link":result["link"]
        }

        for result in results
    ]

    print(urlInfo[0]["link"])

    return urlInfo


def getStream(url):
    with YoutubeDL(ydl_opts) as dl:
        info = dl.extract_info(url,download=False)
        dl.close()

    lang,data = next(filter(lambda data : "orig" in data[0],info["requested_subtitles"].items()))

    soup = BeautifulSoup(get(data["url"]).text,"html.parser")
    texts = soup.find_all("text")

    lyrics = [{"start":line["start"],"dur":line["dur"],"line":line.text} for line in texts]

    print(info["url"])
    return {"stream" : info["url"],"lyrics" : lyrics}


def getSuggestions(query):
    results = suggestions.get(query)["result"]
    return results