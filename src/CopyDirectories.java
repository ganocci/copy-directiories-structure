import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CopyDirectories {

    public static void copiarEstruturaDePastas(Path origem, Path destino) throws IOException {
        Pattern pattern = Pattern.compile("(\\d{2})-(\\d{4})|(\\d{2})-(\\d{2})|(\\d{4})-(\\d{2})");

        Files.walkFileTree(origem, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                String dirName = dir.getFileName().toString();
                Matcher matcher = pattern.matcher(dirName);

                Path destinoDir = null;

                if (matcher.matches()) {
                    int ano = 0;
                    int mes = 0;
                    String novoNome = null;

                    if (matcher.group(1) != null && matcher.group(2) != null) {
                        mes = Integer.parseInt(matcher.group(1));
                        ano = Integer.parseInt(matcher.group(2));
                        ano++;
                        novoNome = String.format("%02d-%04d", mes, ano);
                    }
                    else if (matcher.group(3) != null && matcher.group(4) != null) {
                        mes = Integer.parseInt(matcher.group(4));
                        ano = Integer.parseInt(matcher.group(3));
                        ano += 2000;
                        ano++;
                        novoNome = String.format("%02d-%02d", mes, ano % 100);
                    }
                    else if (matcher.group(5) != null && matcher.group(6) != null) {
                        ano = Integer.parseInt(matcher.group(5));
                        mes = Integer.parseInt(matcher.group(6));
                        ano++;
                        novoNome = String.format("%04d-%02d", ano, mes);
                    }

                    destinoDir = destino.resolve(origem.relativize(dir).getParent()).resolve(novoNome);
                } else {
                    destinoDir = destino.resolve(origem.relativize(dir));
                }

                Files.createDirectories(destinoDir);
                System.out.println("Diretório criado: " + destinoDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String[] args) throws IOException {
        Path origem = Paths.get("caminho/para/diretorio/origem");
        Path destino = Paths.get("caminho/para/diretorio/destino");
        copiarEstruturaDePastas(origem, destino);
    }
}
