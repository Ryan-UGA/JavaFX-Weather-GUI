# ApiApp

![Banner](https://github.com/cs1302uga/cs1302-api-app/raw/main/resources/readme-banner.png)

**Semester**: Spring 2023  
**Version**: v2023.sp  
**Team Size**: 1  
**Server**: Odin

![Team Size Badge](https://img.shields.io/badge/Team%20Size-1-informational) ![Approved for Spring 2023](https://img.shields.io/badge/Approved%20for-Spring%202023-magenta)

---

## Overview

**ApiApp** is a JavaFX application that integrates two external RESTful JSON APIs:  
- [OpenCage Geocoder API](https://opencagedata.com/api)
- [National Weather Service (NWS) API](https://www.weather.gov/documentation/services-web-api)

The user enters a city name, which is first processed by the OpenCage API to obtain its latitude and longitude.  
These coordinates are then used to request a weather forecast from the National Weather Service API.

This application demonstrates chaining API responses:  
- OpenCage provides geographic coordinates.  
- NWS uses those coordinates to find a weekly weather forecast.  
- Two stages of NWS API interaction are handled internally to access final forecast data.

The project is part of the CSCI 1302 Spring 2023 course at the University of Georgia and was designed to reinforce key JavaFX, REST API integration, and object-oriented programming concepts.

---

## Features

- 🔄 **Two Linked APIs**: OpenCage → National Weather Service
- 🌎 **City Input**: Users type a city, automatically retrieving its weather
- 🌦 **Forecast Retrieval**: Displays a 7-day weather forecast for the selected location
- 🔄 **Multiple Searches**: Allows users to input multiple cities in one session without restarting the application
- 📚 **Fully Object-Oriented**: Clean, modular Java design
- 📜 **Exception Handling**: Gracefully manages invalid inputs and API errors
- 🎨 **JavaFX UI**: Simple, clean interface

---

## Technologies Used

- Java 17
- JavaFX 17
- Maven
- Gson (for JSON parsing)
- OpenCage Geocoder API
- National Weather Service API
- Odin Linux Environment

---

## Setup and Running

1. **Clone the Repository**
   ```bash
   git clone git@github.com:your-username/your-repo-name.git
   cd your-repo-name

## Learning Outcomes
- Develop a complete Java application with object-oriented principles

- Utilize inheritance and polymorphism

- Implement robust exception handling

- Design and develop a JavaFX GUI

- Parse and combine JSON data from multiple APIs
