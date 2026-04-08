/*

Keybinds:

"Z" to toggle on/off

Escape to end program (panic key)


How it works for nerds:

Program scans your screen in a certain rectangle area where targets are expected to spwan

In this area any pixels that are detected to be a certain color (RGB value of 149, 195, 232) and clicks on these pixels

Scanning every single pixel would be slow and take a lot of cpu power so the program skips some pixels (30 at a time)
to speed up the process and utilize less cpu power


Line 31 of Aimbot.java to edit the color code if needed

Line 32 of Aimbot.java to edit the size of scanned area if needed

*/
