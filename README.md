# ParcelSorter-Smart Logistics Center Simulation

## About the Project
ParcelSorter simulates a smart logistics center where parcels are received, categorized, and dispatched according to their destination city, size, and priority.
This project blends theoretical knowledge of data structures with practical simulation logic, aiming for modularity, correctness, and detailed reporting.

## About Working
ParcelSorter is a simulation so parcels are generating randomly. The project actually offers a working method.

## Features

**Dynamic Parcel Management:** Parcels are processed based on real-time logistics conditions.

**City-Based Routing:** Parcels are sorted and directed to their respective cities.

**Priority & Size Classification:** Parcels are handled according to their priority levels and sizes.

**Rich Use of Data Structures:**

Queues for incoming parcel buffering

Stacks for dispatch management

Binary Search Trees (BSTs) for city sorting

Hash Tables for fast city lookups

Circular Linked Lists for maintaining city connections

## Setup and Running

1. Clone the repository or download it as a ZIP:
   ```bash
   git clone https://github.com/meryemkiremitci/ParcelSorter.git
   ```
2. Navigate to the project folder: 
   ```bash
   cd MazeGame
   ```
3. Compile the Java files:  
   ```bash
   javac *.java
   ```
4. Start the game:  
   ```bash
   java Main2
   ```
## Configuration File

The simulation reads values from a config.txt file, allowing the user to customize:
2.	Size of the queue for arrived parcels
3.	Number of ticks to be executed
4.	Minimum number of parcels generated per tick
5.	Maximum number of parcels generated per tick
6.	Return probability of parcels
7.	Cities
8.	City queue size
9.	How often the return stack will be emptied (in ticks)

## Logging
The simulation writes detailed logs to one or more .txt files. (DispatchedReturnedsAndReport.txt and simulation_log.txt)

These include logs for parcel dispatching, returns, and system reports.

Logging helps with analyzing simulation behavior and debugging.

## License

Released under the MIT License.
---

