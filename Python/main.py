from youtube_dl import *
from youtubesearchpython import *



ydl_opts =  {
    'format': 'm4a/bestaudio',
}



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
    dl = YoutubeDL(ydl_opts)
    info = dl.extract_info(url,download=False)
    return info["url"]