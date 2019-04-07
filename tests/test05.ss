func printNN(n) {
	write n * n
}

func printNTwice(n) {
	@USE printNN
	
	write n
	write n
}

i: 9
printNTwice(i)

a: "Hello"
b: "World"

write a.b