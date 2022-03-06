# Bike-Catalog
Bike Catalog build in android using MVVM, Databinding, Hilt

A mobile solution for a bike catalog with bike detail view.
This app provides different filters and sorting mechanisms for searching bikes. 
The data being fetched is from a custom API specifically made for bikes data. 

### MVVM Pattern (Model View ViewModel)

### Architecture
MVVM Architecture (View - DataBinding - ViewModel - Model)
For insuring sepration of concern in the app for extensibility and testability, I am using MVVM architecture.

### Description

- Added dependency injection with Hilt

- Added View Binding

- Added Data Binding to bind observable data to UI elements.

### Dependencies

- Retrofit (Network Calling)
- coroutines (Multi Threading)
- navigation component (Navigation between components)
- glide (To load images in imageView)
- Dagger-Hilt (alpha) (dependency injection)
