func printNN(n) {
	write n * n
}

func printNTwice(n) {
	@USE printNN
	
	write n
	write n
}

i: 4.5
i++
printNTwice(i)

i--
printNTwice(i)
