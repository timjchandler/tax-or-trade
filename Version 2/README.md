# Version 2

At this point the version is not yet working as intended. Some parts have been removed, others are being rewritten.

In this iteration I am making sure to thoroughly unit test additions to identify issues earlier on, rather than being surprised by them further down the line

### What is working:
+ The power types read in and set values correctly
+ The power split reads in and sets up the agents correctly
+ The csv is being saved in a dynamically updating manner

### What is not working:
+ The tax is not fully implemented here, some changes have been made to remove bugs and not yet replaced

### What is yet to be successfully implemented:
+ The trade system. The auction system previously used did not work correctly
+ The GUI, this can largely be brought across from the previous version once the model works
+ Instead of simplified values, this iteration is based around using real world values:
    - GWh for power 
    - Actual currency values for money (euros most likely, possibly dollars)
    - kg for carbon
  
