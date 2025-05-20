import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Não é necessário importar Conexao se estiver no mesmo pacote.
// Se estiver em um pacote diferente, por exemplo, com.petstop.db:
// import com.petstop.db.Conexao;

/**
 * Classe principal para iniciar a aplicação PetStop.
 * Responsável por:
 * 1. Testar a conexão com o banco de dados.
 * 2. Iniciar a interface gráfica Swing.
 * 🐶🐱🛒
 */
public class Main {

    public static void main(String[] args) {
        // Passo 1: Tentar conectar ao banco de dados usando a classe Conexao.
        System.out.println("INFO: Tentando conectar ao banco de dados PetStop...");
        Connection conn = Conexao.conectar();

        if (conn == null) {
            // A classe Conexao.conectar() já imprime "Erro ao conectar: ..."
            // Aqui, adicionamos uma mensagem visual para o usuário e encerramos.
            mostrarMensagemErroFatal(
                "Falha crítica ao conectar ao banco de dados.\n" +
                "Verifique se o servidor MySQL está em execução, se o banco 'petstop' (ou 'PetStop') existe,\n" +
                "e se as credenciais em Conexao.java (usuário: " + Conexao.USUARIO + ") estão corretas.\n" +
                "Consulte o console para mais detalhes do erro JDBC.\n\n" +
                "A aplicação será encerrada."
            );
            System.err.println("ERRO FATAL: Conexão com o banco de dados não estabelecida. Encerrando aplicação.");
            return; // Encerra a aplicação se não conseguir conectar.
        }

        System.out.println("INFO: Conexão com o banco de dados PetStop estabelecida com sucesso! ✅");

        // Passo 2: (Opcional, mas recomendado) Verificar se as tabelas principais existem.
        // Isso ajuda a confirmar se o script.sql foi executado corretamente.
        try {
            System.out.println("INFO: Verificando tabelas...");
            boolean tabelasOk = verificarTabelas(conn);
            if (!tabelasOk) {
                mostrarMensagemErroFatal(
                    "Uma ou mais tabelas essenciais (animais, produtos) não foram encontradas no banco de dados.\n" +
                    "Por favor, execute o script.sql para criar a estrutura do banco.\n\n" +
                    "A aplicação será encerrada."
                );
                Conexao.fechar(conn); // Fecha a conexão antes de sair.
                System.err.println("ERRO FATAL: Tabelas não encontradas. Encerrando aplicação.");
                return;
            }
            System.out.println("INFO: Tabelas 'animais' e 'produtos' parecem estar presentes. 👍");
        } finally {
            // É importante fechar esta conexão de teste inicial.
            // A interface gráfica ou as classes DAO abrirão e fecharão suas próprias conexões conforme necessário.
            Conexao.fechar(conn);
            System.out.println("INFO: Conexão de teste inicial com o banco de dados foi fechada.");
        }

        // Passo 3: Iniciar a interface gráfica Swing na Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Define um Look and Feel para uma aparência mais moderna ou nativa do sistema.
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                    System.err.println("AVISO: Não foi possível definir o Look and Feel do sistema: " + e.getMessage());
                }

                System.out.println("INFO: Iniciando interface gráfica do PetStop...");

                // --- PONTO DE INTEGRAÇÃO DA SUA TELA PRINCIPAL ---
                // Aqui você deve instanciar e exibir a sua janela principal (JFrame).
                // Substitua o código de placeholder abaixo pela sua implementação.

                // Exemplo de como você instanciaria sua tela principal (descomente e adapte):
                /*
                TelaPrincipal telaPrincipal = new TelaPrincipal(); // Assumindo que sua classe se chama TelaPrincipal
                telaPrincipal.setTitle("PetStop - Gerenciamento 🐶🐱🛒");
                // Configurações como setDefaultCloseOperation, pack, setLocationRelativeTo, setVisible(true)
                // devem ser feitas no construtor da TelaPrincipal ou aqui.
                telaPrincipal.setVisible(true);
                */

                // --- INÍCIO DO CÓDIGO DE PLACEHOLDER ---
                // Se você ainda não tem sua tela principal, este placeholder exibirá uma janela simples.
                // Remova ou comente esta parte quando sua TelaPrincipal estiver pronta.
                JFrame placeholderFrame = new JFrame("PetStop - Gerenciamento (Em Desenvolvimento)");
                placeholderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                placeholderFrame.setSize(600, 400); // Tamanho da janela de placeholder

                JLabel placeholderLabel = new JLabel(
                    "<html><div style='text-align: center;'>" +
                    "<h1>🐶 PetStop Gerenciamento 🐱🛒</h1>" +
                    "<p>Interface Principal será carregada aqui.</p>" +
                    "<p>Conexão com o banco de dados testada com sucesso.</p>" +
                    "<p>Tabelas 'animais' e 'produtos' verificadas.</p>" +
                    "<br><p><em>Desenvolva sua TelaPrincipal e integre-a em Main.java.</em></p>" +
                    "</div></html>",
                    SwingConstants.CENTER
                );
                placeholderFrame.add(placeholderLabel);
                placeholderFrame.setLocationRelativeTo(null); // Centraliza a janela
                placeholderFrame.setVisible(true);
                // --- FIM DO CÓDIGO DE PLACEHOLDER ---

                System.out.println("INFO: Interface gráfica do PetStop (ou placeholder) iniciada.");
            }
        });
    }

    /**
     * Verifica a existência das tabelas 'animais' e 'produtos' no banco de dados.
     * @param conn Conexão ativa com o banco de dados.
     * @return true se ambas as tabelas existirem, false caso contrário.
     */
    private static boolean verificarTabelas(Connection conn) {
        boolean animaisOk = false;
        boolean produtosOk = false;

        try (Statement stmt = conn.createStatement()) {
            // Verifica a tabela 'animais'
            try (ResultSet rs = stmt.executeQuery("SELECT 1 FROM animais LIMIT 1")) {
                animaisOk = true; // Se a query não lançar exceção, a tabela existe
            } catch (SQLException e) {
                System.err.println("ERRO: Tabela 'animais' não encontrada ou inacessível. " + e.getMessage());
            }

            // Verifica a tabela 'produtos'
            try (ResultSet rs = stmt.executeQuery("SELECT 1 FROM produtos LIMIT 1")) {
                produtosOk = true; // Se a query não lançar exceção, a tabela existe
            } catch (SQLException e) {
                System.err.println("ERRO: Tabela 'produtos' não encontrada ou inacessível. " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("ERRO: Falha ao criar Statement para verificar tabelas. " + e.getMessage());
            return false; // Falha geral na verificação
        }
        return animaisOk && produtosOk;
    }

    /**
     * Exibe uma mensagem de erro fatal usando JOptionPane.
     * Garante que seja executado na Event Dispatch Thread se necessário.
     * @param mensagem A mensagem de erro a ser exibida.
     */
    private static void mostrarMensagemErroFatal(String mensagem) {
        Runnable exibirErro = () -> JOptionPane.showMessageDialog(
            null,
            mensagem,
            "Erro Crítico - PetStop",
            JOptionPane.ERROR_MESSAGE
        );

        if (SwingUtilities.isEventDispatchThread()) {
            exibirErro.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(exibirErro);
            } catch (Exception e) {
                // Em último caso, se o SwingUtilities falhar (improvável aqui), imprime no console.
                System.err.println("ERRO CRÍTICO ADICIONAL AO MOSTRAR MENSAGEM: " + mensagem);
                e.printStackTrace();
            }
        }
    }
}
