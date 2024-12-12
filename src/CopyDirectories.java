import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CopyDirectories {

    public static void copiarEstruturaDePastas(Path origem, Path destino) throws IOException {
        Pattern pattern = Pattern.compile("(\\d{2})-(\\d{4})|(\\d{2})-(\\d{2})|(\\d{4})-(\\d{2})");
        Map<Path, Path> pathMap = new HashMap<>();

        Files.walkFileTree(origem, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                String dirName = dir.getFileName().toString();
                Matcher matcher = pattern.matcher(dirName);

                Path destinoDir;

                // Calcula o diretório de destino, ignorando a raiz
                if (dir.equals(origem)) {
                    destinoDir = destino; // Não cria um subdiretório com o nome da origem
                } else {
                    if (matcher.matches()) {
                        int ano = 0;
                        int mes = 0;
                        String novoNome = null;

                        if (matcher.group(1) != null && matcher.group(2) != null) {
                            mes = Integer.parseInt(matcher.group(1));
                            ano = Integer.parseInt(matcher.group(2));
                            ano++;
                            novoNome = String.format("%02d-%04d", mes, ano);
                        } else if (matcher.group(3) != null && matcher.group(4) != null) {
                            mes = Integer.parseInt(matcher.group(4));
                            ano = Integer.parseInt(matcher.group(3));
                            ano += 2000;
                            ano++;
                            novoNome = String.format("%02d-%02d", mes, ano % 100);
                        } else if (matcher.group(5) != null && matcher.group(6) != null) {
                            ano = Integer.parseInt(matcher.group(5));
                            mes = Integer.parseInt(matcher.group(6));
                            ano++;
                            novoNome = String.format("%04d-%02d", ano, mes);
                        }

                        destinoDir = pathMap.getOrDefault(dir.getParent(), destino).resolve(novoNome);
                    } else {
                        destinoDir = pathMap.getOrDefault(dir.getParent(), destino).resolve(dir.getFileName());
                    }
                }

                // Cria o diretório no destino
                Files.createDirectories(destinoDir);
                pathMap.put(dir, destinoDir); // Atualiza o mapeamento para subpastas
                System.out.println("Diretório criado: " + destinoDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                // Ignora os arquivos
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void main(String[] args) throws IOException {
        Path origem = Paths.get("caminho/para/diretorio/origem"); // Substitua pelo caminho de origem
        Path destino = Paths.get("caminho/para/diretorio/destino"); // Substitua pelo caminho de destino
        copiarEstruturaDePastas(origem, destino);
    }
}
