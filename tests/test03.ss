func fib(n) {
	@USE newfunction

   	if (n EQUAL TO 0 OR n EQUAL TO 1) {
  	    r: n
  	} else {
    	  r: fib(n - 1) + fib(n - 2)
	}
   	ret r
}


func newfunction(n) {
	write n
}


for (i: 0; i LESS THAN 35; i: i + 1) {
    write fib(i)
}
