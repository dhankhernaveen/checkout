# checkout

This checkout system is build to work with/without promotion rules.

For this exercies we are fetching available items & promotions through json files.

Checkout is build with 2 methods
  - Scan
  - Total
  
  Scan checks & validates that the Item we are trying to add is  valid & has all mandtory details. 
  And also checks if the item is present in the catalogue.
  
  Total will be calculated based on conditions if the promotion was applicable or not.
  
 Currently we have 2 types of promotions 
  1) Which Reduces the value of each items based on quantities
  2) Which is applied on the total basket cost
   We read both the promotions from json files to keep them flexiable.

In future we can also introduce another type of promotion and extend our functionality or we can remove any promotions.
