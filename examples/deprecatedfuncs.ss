func printNN(n) {
	write n * n
}

func printNTwice(n) {
	@USE printNN
	
	write n
	write n
}

i: 9
//++i
i++
printNTwice(i)

i++

printNN(i)