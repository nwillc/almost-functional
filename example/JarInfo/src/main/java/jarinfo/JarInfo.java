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
        final JarFile jarFile = new JarFile(get(newIterable(args),0).orElseThrow("Requires a jar file as first argument"));
        final String searchName = get(newIterable(args), 1).orElse("META-INF/MANIFEST.MF");
        final JarEntry searchResult = find(iterable(jarFile.entries()), new Predicate<JarEntry>() {
            @Override
            public boolean test(JarEntry jarEntry) {
                return jarEntry.getName().equals(searchName);
            }
        }).orElseThrow("Could not find " + searchName + " in jar");


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
