# OS Process Scheduler (SRTF) 

## Project Overview
This project is a **Java simulation** of the **Shortest Remaining Time First (SRTF)** CPU scheduling algorithm (also known as preemptive SJF).

The goal of the project is to demonstrate how an operating system schedules processes, handles preemption, and manages CPU time efficiently.

---

## Key Features
* **Preemptive Scheduling:** The CPU is always assigned to the process with the shortest *remaining* execution time.
* **Context Switching:** Simulates a realistic context switch overhead of **1 ms** between processes.
* **Dynamic Process Handling:** Utilizes a **Priority Queue** to manage process arrivals and ready states efficiently.
* **Performance Evaluation:** The simulator calculates and displays key metrics:
    * Average Waiting Time
    * Average Turnaround Time
    * CPU Utilization

---

## Technical Highlights
* **Data Structures:** Uses Java’s `PriorityQueue` with a custom `Comparator` to efficiently select the next process based on remaining time.
* **Object-Oriented Design:** The system is organized using separate `Process` and `Event` classes, ensuring the code is clean, modular, and maintainable.

---

## Contributors
This project was developed by:
- Haneen Alaqeel
- Lama Fahad
- Yara Alanazi
- Wajd Alquwayi
- Razan Rashid