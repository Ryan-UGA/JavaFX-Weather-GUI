# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice looking HTML.

## Part 1: App Description

> Please provide a firendly description of your app, including the
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

The user can enter a city and click the button to get the weather forecast
for the next ten time periods. There is some exception handling for
multiple cities, cities not in the United States, and other bad inputs.

The ApiApp includes two API's, OpenCage and the National Weather Service.
OpenCage is the API that will convert a city entered by the user into
latitude and longitude. Then, these coordinates are inputted into the
National Weather Service's uri to get the weather forecast. It is not possible
to enter a city as a parameter for the National Weather Service uri because the
webpage was never found, so the OpenCage API was needed to convert the city into
latitude and longitude which are valid parameters for the National Weather Service.

There were three responses generated using JSON formatting. The first response was
from OpenCage's API which converted the city into latitude and longitude.
The second and third responses were from the National Weather Service's API.
The National Weather Service's API requires two different responses
before reaching the data giving the weather's weekly forecast. The first response
will get a variety of data, including links to the weekly and hourly weather
forecast. I pulled the link giving the weekly weather forecast.
Then, this link is the uri for the final response.

GitHub 'https' URL: https://github.com/Javaprogrammer2023/cs1302-api.git

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

I learned how to extract and organize data with the API's using JSON.
This project sparked my interest in data analytics and programming even more,
so I am happy that I got to learn and understand the JSON formatting.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

If I restarted the project, I would have read the JSON reading before
beginning this project. I couldn't make it to class on the day that JSON
was the topic for lecture, so I did not understand JSON that well. I kind of
understood the logic for the ItunesResponse class, but this project had objects
inside objects which got confusing. If I understood JSON better before beginning,
I would have figured out the JSON formatting quicker and with less frustration.
