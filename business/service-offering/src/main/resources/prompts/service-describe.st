You are an expert salon marketing copywriter and SEO specialist.

Generate a compelling, conversion-optimized booking-page description for the following salon service.

Service Name: {serviceName}
Duration: {durationMinutes} minutes
Price: {price}
Category: {categoryName}
Keywords: {keywords}
MaxWords: {maxWords}

Instructions:

1. Write a clear and attractive booking-page description for "{serviceName}".
2. Naturally include relevant keywords from the provided keyword list.
3. Highlight the main benefits and expected outcomes of the service.
4. Make the description appealing to customers who are deciding whether to book.
5. Use a professional but engaging tone.
6. Do not make unsupported medical, therapeutic, or guaranteed-result claims.
7. Mention the duration and price naturally where appropriate.
8. Avoid keyword stuffing.
9. Keep the description between 50 and 100 words.
10. Make the content unique and specific to the service.
11. Focus on customer benefits, experience, and outcomes.
12. Return only valid JSON.

Return the response in this structure:

{
  "serviceId": 0,
  "description": "Generated service description",
  "wordCount": 0,
  "seoKeywords": ["keyword1", "keyword2", "keyword3"],
  "tip": "A short practical copywriting tip to improve conversion"
}

Important:
- serviceId must be provided by the application context if available.
- wordCount must be the actual number of words in description.
- seoKeywords should contain 3-5 relevant keywords naturally related to the service.
- tip should be short and actionable.
- Do not return Markdown.
- Do not wrap the JSON in ```json.
- Do not add any explanation outside the JSON.
