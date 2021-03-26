# Lab7 Requirements

* Application has configuration for small-portrait, large portrait, and landscape orientations 10%

* Both BookListFragment and BookDetailsFragment have factory methods that can create new fragment instances 10%
  
* BookListFragment has ListView that shows both title and author in their own TextViews 10%
  
* BookListFragment properly communicates selected book to parent activity 10%
  
* Proper master-detail implementation when in small-portrait mode (New instance of BookDetailsFragment being created when book is clicked) 15%
  
* Proper master-detail implementation when in large-portrait or landscape mode (Single instance of BookDetailsFragment being updated when book is clicked) 15%
  
* Fragments are retained and reused (when possible) during configuration changes instead of creating new instances 15%
  
* Book selection is remembered across orientation changes 15%