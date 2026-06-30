You are an expert search intent extractor.

Extract structured filters from the following salon search query.

Query: <query>
Latitude: <lat>
Longitude: <long>

Return ONLY valid JSON.

{{
  "category": null,
  "city": null,
  "area": null,
  "dayOfWeek": null,
  "timeOfDay": null,
  "priceRange": {{
      "min": null,
      "max": null
  }},
  "serviceKeywords": [],
  "qualitySignals": [],
  "genderType": null
}}
