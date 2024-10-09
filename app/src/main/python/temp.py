from yt_dlp import YoutubeDL
from bs4 import BeautifulSoup
from requests import get



ydl_opts =  {
    'format': 'm4a/bestaudio',
    "allsubtitles":True,
    'writesubtitles': True,
    'writeautomaticsub': True,
    'subtitlesformat': 'srv1'
}

dl = YoutubeDL(ydl_opts)
info = dl.extract_info("https://youtu.be/vnsEKd5z_rA?si=PrQrAsLh4jAEeTrl",download=False)
subtitles = list(filter(lambda data : "orig" in data[0],info["requested_subtitles"].items()))
lang,data = subtitles[0]
soup = BeautifulSoup(get(data["url"]).text,"html.parser")
texts = soup.find_all("text")
lyrics = [{"start":line["start"],"dur":line["dur"],"line":line.text} for line in texts]
print(lyrics)