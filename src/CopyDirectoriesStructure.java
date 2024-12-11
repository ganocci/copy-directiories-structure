import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class CopyDirectoriesStructure {

    private Path origem;
    private Path destino;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CopyDirectoriesStructure().criarGUI();
        });
    }

    public void criarGUI() {
        JFrame frame = new JFrame("Copiar Estrutura de Pastas");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());

        JButton btnEscolherOrigem = new JButton("Escolher Pasta de Origem");
        JButton btnEscolherDestino = new JButton("Escolher Pasta de Destino");
        JButton btnIniciarCopia = new JButton("Iniciar Cópia");

        btnEscolherOrigem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retorno = fileChooser.showOpenDialog(frame);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                origem = fileChooser.getSelectedFile().toPath();
                showMessage("Origem Selecionada", "Pasta de origem: " + origem.toString());
            }
        });

        btnEscolherDestino.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retorno = fileChooser.showOpenDialog(frame);
            if (retorno == JFileChooser.APPROVE_OPTION) {
                destino = fileChooser.getSelectedFile().toPath();
                showMessage("Destino Selecionado", "Pasta de destino: " + destino.toString());
            }
        });

        btnIniciarCopia.addActionListener(e -> {
            if (origem != null && destino != null) {
                try {
                    CopyDirectories.copiarEstruturaDePastas(origem, destino);
                    showMessage("Sucesso", "Estrutura de diretórios copiada com sucesso!");
                } catch (IOException ex) {
                    showMessage("Erro", "Erro ao copiar estrutura de diretórios: " + ex.getMessage());
                }
            } else {
                showMessage("Erro", "Por favor, selecione as pastas de origem e destino.");
            }
        });

        frame.add(btnEscolherOrigem);
        frame.add(btnEscolherDestino);
        frame.add(btnIniciarCopia);

        frame.setVisible(true);
    }

    private void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}
