func printNN(n) {
	n: n * n
	ret n
}

func printN(n) {
	use(printNN)
	n: n + 2
	ret n
}

i: rand(1, 250)
// Would return x, but instead now returns x*x
write("Random number is "+i)
write(printN(i))