from youtube_dl import *
from youtubesearchpython import *



ydl_opts =  {
    'format': 'm4a/bestaudio',
}

dl = YoutubeDL(ydl_opts)
suggestions = Suggestions(language = "en", region = "IN", timeout = None)



def getUrlsInfo(query,limit=20,lang="en",region="IN",timeout=None):
    search = VideosSearch(query,limit,lang,region,timeout)
    results = search.result()["result"]

    urlInfo = [
        {
            "title" : result["title"],
            "publishedTime":result["publishedTime"],
            "publishedTime":result["publishedTime"],
            "thumbnail":result["thumbnails"][0]["url"],
            "channelName":result["channel"]["name"],
            "channelThumnail": result["channel"]["thumbnails"][0]["url"],
            "link":result["link"]
        }

        for result in results
    ]

    return urlInfo


def getStream(url):
    info = dl.extract_info(url,download=False)
    return info["url"]


def getSuggestions(query):
    results = suggestions.get(query)["result"]
    return results