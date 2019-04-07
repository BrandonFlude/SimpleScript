func test(n) {
	ret n*n
}

func fib(n) {
	//@USE test (will print all square numbers)
   	if (n EQUAL TO 0 OR n EQUAL TO 1) {
  	    r: n
  	} else {
    	  r: fib(n - 1) + fib(n - 2)
	}
   	ret r

}

for (i: 0; i LESS THAN 10; i: i + 1) {
    write fib(i)
}
