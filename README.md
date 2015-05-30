# Java ThreadPools App
This application was developed as part of my undergraduate course work for a module called Performance Based Programming. The module was largely focused on the safe developemnet of multi-threaded applications in Java. 

This assignemnt specifically investigated the concepts of Thread Pools and Synchronisation. The application provides the user with a GUI interface where they can create a fixed thread pool with a bounded queue of their own specification. The user initialises a thread pool then submits tasks (of a specified length in millis) to be
processed by the threads in the pool. When a task is processed it appends to to a log file and a text
area on the GUI. As there is many threads writing to the same file, potentially at the same time, a
synchronised method is employed. When they the user attempts to add more tasks than the queue
and thread pool can hole they are notified.

A full report can be found in the docs directory this repository. It includes: further discussion of the concepts investigated; discussion regarding the wider implications of both Thread Pools and Syncronisation in Java programming; conclusion and results are then presented.

