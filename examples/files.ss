filename: "examples/test.txt"

write("Test 1 (maybe edit this file before run if it's blank)")
// Open, read and edit the file using var from the root
openfile(filename)
readfile
editfile("This is some example text...")
closefile


write ("Test 2")
// Reopen file using hardtyped name from a directory
openfile("examples/test.txt")
readfile

// Clear and prepare file for next run
clearfile

// Close
closefile