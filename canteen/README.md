# Canteen
A producer-consumer problem rendition using a chef(producer) in a canteen but with only one student(consumer)

## Problem
The chef of a Canteen prepares 4 different kinds of Foods while the Students order the foods through  their mobile. However there are limited tables to server at a time. The chef and students are not blocking each other and thus must run in separate threads, Student must wait if the table is empty while the chef must wait if the table is full.

A partial implementation is attached. Read the code carefully and complete the implementation. Don't consider the order part for this assignment. Consider a single student who will consume all the foods placed in any of the table. Chef produces random foods at random delays in tables in a round robin fashion which are consumed at random intervals sequentially by the student. The states of the chef and students ( e.g. Chef waiting for student, Student waiting for chef, Chef produced Mango, Student consumed Banana etc) should be displayed next to their icon.

### Features covered
- multithreading
- producer-consumer

## Run
Run the MainFrame class.