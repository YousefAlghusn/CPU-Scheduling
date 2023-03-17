# CPU-Scheduling
An app that uses different scheduling algorithms for processes.

The algorithms are: Priority Scheduling, SRTF, and Round Rubin.

# Example: 

Priority Scheduling:
____________________________________________________________________________________________________________________
|Job102    |Job106     |Job104                   |Job107               |Job101       |Job105  |Job103              |
0——————————4———————————9—————————————————————————28————————————————————43————————————50———————52———————————————————66
Average waiting: 26.57 ms
Average completion: 36.00 ms

SRTF:
______________________________________
|P4 |P2  |P1 |P2  |P3  |P5   |P4     |
0———1————3———4————6————8—————11——————16
Average waiting: 3.80 ms
Average completion: 9.00 ms

Round Rubin:
_______________________________________________
|P1   |P2 |P3  |P4 |P5   |P1   |P5  |P1   |P1 |
0—————3———4————6———7—————10————13———15————18——19
P1: turnAround: 19.0
P2: turnAround: 4.0
P3: turnAround: 6.0
P4: turnAround: 7.0
P5: turnAround: 15.0
Average waiting: 6.40 ms
Average completion: 10.20 ms
