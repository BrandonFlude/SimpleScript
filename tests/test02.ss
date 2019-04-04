integer1: 3
integer2: 7
dec1: 1.5
result: 0
result2: 0

// Check numbers match, if they don't - add them together.

if(integer1 NOT EQUAL TO integer2)
{ 
	result: integer1 + dec1
	result2: dec1 + integer1
}

write result
write result2