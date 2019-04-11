func test(n) {
	ret n*n
}

func fib(n) {
	//@USE test (will print all square numbers up to max i)
   	if (n EQUAL TO 0 OR n EQUAL TO 1) {
  	    r: n
  	} else {
    	  r: fib(n - 1) + fib(n - 2)
	}
   	ret r

}

write "Attempt 1"

for (i: 0; i LESS THAN 10; i++) {
    write fib(i)
}