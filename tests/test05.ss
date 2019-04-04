func printNN(n) {
	write n * n
}

func printNTwice(n) {
	@USE printNN
	
	write n
	write n
}

i: 3
++i
printNTwice(i)