# newsRoom


![banner1n](https://user-images.githubusercontent.com/66402794/160296647-1746aae7-f028-4bc6-8dad-ff7c9d1a4bfa.png)
![banner2n](https://user-images.githubusercontent.com/66402794/160296674-05a4ffd6-1874-459e-96cf-1fdcbdccf69c.png)
![banner3](https://user-images.githubusercontent.com/66402794/160296700-89a362b9-a222-4bdd-b3b5-6781fc3199d8.png)

# configuring
just replace a value of queryParameter in com.alzu.android.newsroom.api.RetrofitInstance to yours (you need to get apiKey in newsapi.org)
and edit app gradle file (delete lines 9-11 & 26-34) or create apikey.properties in root dir & add lines like API_KEY="your apikey" inside it
(in my case I added 9 keys, but you can add your quantity, don't forget to edit app gradle file, for instance, delete extra lines: buildConfigField(...),
leave one if you have 1 api-key) and rebuild the project
