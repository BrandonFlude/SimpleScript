echo "=== If Statements ==="
java -classpath ./bin SimpleScript < examples/ifstatement.ss

echo "=== Maths With Different Types ==="
java -classpath ./bin SimpleScript < examples/maths.ss

echo "=== Fibonacci Sequence ==="
java -classpath ./bin SimpleScript < examples/fib.ss

echo "=== Sorting Arrays ==="
java -classpath ./bin SimpleScript < examples/arrays.ss

echo "=== Values from Dictionaries ==="
java -classpath ./bin SimpleScript < examples/dictionaries.ss

echo "=== Deprecated Functions ==="
java -classpath ./bin SimpleScript < examples/deprecatedfuncs.ss

echo "=== String Concatenation ==="
java -classpath ./bin SimpleScript < examples/stringconcat.ss

echo "=== Reading Files ==="
java -classpath ./bin SimpleScript < examples/files.ss

echo "=== Main Example ==="
java -classpath ./bin SimpleScript < examples/realExample.ss

PAUSE