# Example
The following is a useful example, the emphasis was on using AF, so it is contrived in spots. That said, worth note are:

* Using `Optional` instances to cleanly deal with nulls.
* Using `Optional`'s `orElse` and `orElseSupplier` methods to deal with error cases in the argument parsing.
* Searching the jars entries based on a `Predicate` to match the given argument.
* Using `SupplierIterable`, and a simple `Consumer` to output the file's contents.  
* Using `Iterables` methods on `Iterable` instances based on arrays and enumerations. 

-----
  
	package jarinfo;
	
	import almost.functional.*;
	import almost.functional.utils.SupplierIterable;
	
	import java.io.BufferedReader;
	import java.io.InputStream;
	import java.io.InputStreamReader;
	import java.util.jar.JarEntry;
	import java.util.jar.JarFile;
	
	import static almost.functional.utils.ArrayIterable.newIterable;
	import static almost.functional.utils.Iterables.*;
	
	public class JarInfo {
	    public static void main(String[] args) throws Exception {
	        final JarFile jarFile = new JarFile(get(newIterable(args),0).orElseSupplier(new Supplier<String>() {
	            @Override
	            public String get() {
	                throw new IllegalArgumentException("Requires a jar file as first argument");
	            }
	        }));
	
	        final String searchName = get(newIterable(args), 1).orElse("META-INF/MANIFEST.MF");
	
	        final JarEntry searchResult = find(iterable(jarFile.entries()), new Predicate<JarEntry>() {
	            @Override
	            public boolean test(JarEntry jarEntry) {
	                return jarEntry.getName().equals(searchName);
	            }
	        }).orElseSupplier(new Supplier<JarEntry>() {
	            @Override
	            public JarEntry get() {
	                throw new IllegalArgumentException("Manifest " + searchName + " not found!");
	            }
	        });
	
	        try (InputStream inputStream = jarFile.getInputStream(searchResult);
	             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
	           forEach(new SupplierIterable<>(new Supplier<Optional<String>>() {
	                @Override
	                public Optional<String> get() {
	                    try {
	                        return Optional.ofNullable(reader.readLine());
	                    } catch (Exception e) {
	                        throw new RuntimeException(e);
	                    }
	                }
	            }), new Consumer<String>() {
	               @Override
	               public void accept(String s) {
	                   System.out.println(s);
	               }
	           });
	        }
	    }
	}