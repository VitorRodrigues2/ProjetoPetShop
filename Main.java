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

/**
 * Classe principal para iniciar a aplicação PetStop.
 * Responsável por:
 * 1. Testar a conexão com o banco de dados.
 * 2. Iniciar a interface gráfica Swing.
 */
public class Main {

    public static void main(String[] args) {
        //Tentar conectar ao banco de dados, usando a classe Conexao.
        System.out.println("INFO: Tentando conectar ao banco de dados PetStop...");
        Connection conn = Conexao.conectar();

        //Se não conseguir conectar, exibo erro e encerro o programa
        if (conn == null) {
            mostrarMensagemErroFatal(
                "Falha crítica ao conectar ao banco de dados.\n" +
                "Verifique se o servidor MySQL está em execução, se o banco 'petstop' (ou 'PetStop') existe,\n" +
                "e se as credenciais em Conexao.java (usuário: " + Conexao.USUARIO + ") estão corretas.\n" +
                "Consulte o console para mais detalhes do erro JDBC.\n\n" +
                "A aplicação será encerrada."
            );
            System.err.println("ERRO FATAL: Conexão com o banco de dados não estabelecida. Encerrando aplicação.");
            return;
        }

        System.out.println("INFO: Conexão com o banco de dados PetStop estabelecida com sucesso! ✅");

        //Verificar se as tabelas principais existem
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
            Conexao.fechar(conn);
            System.out.println("INFO: Conexão de teste inicial com o banco de dados foi fechada.");
        }

        //Início da interface gráfica (EDT)
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

                PetStopUI telaPrincipal = new PetStopUI();
                telaPrincipal.setTitle("PetStop - Gerenciamento 🐶🐱🛒");
                telaPrincipal.setVisible(true);

                JFrame placeholderFrame = new JFrame("PetStop - Gerenciamento (Em Desenvolvimento)");
                placeholderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                placeholderFrame.setSize(600, 400); //Tamanho da janela de placeholder

                JLabel placeholderLabel = new JLabel(
                    "<html><div style='text-align: center;'>" +
                    "<h1>🐶 PetStop Gerenciamento 🐱🛒</h1>" +
                    "<p>Conexão com o banco de dados testada com sucesso.</p>" +
                    "<p>Tabelas 'animais' e 'produtos' verificadas.</p>" +
                    "</div></html>",
                    SwingConstants.CENTER
                );
                placeholderFrame.add(placeholderLabel);
                placeholderFrame.setLocationRelativeTo(null); //Centraliza a janela
                placeholderFrame.setVisible(true);

                System.out.println("INFO: Interface gráfica do PetStop (ou placeholder) iniciada.");
            }
        });
    }

    //Verificar se as tabelas 'animais' e 'produtos' existem no banco
    private static boolean verificarTabelas(Connection conn) {
        boolean animaisOk = false;
        boolean produtosOk = false;

        try (Statement stmt = conn.createStatement()) {
            //Verifica a tabela 'animais'
            try (ResultSet rs = stmt.executeQuery("SELECT 1 FROM animais LIMIT 1")) {
                animaisOk = true; //Se a query não lançar exceção, a tabela existe
            } catch (SQLException e) {
                System.err.println("ERRO: Tabela 'animais' não encontrada ou inacessível. " + e.getMessage());
            }

            //Verifica a tabela 'produtos'
            try (ResultSet rs = stmt.executeQuery("SELECT 1 FROM produtos LIMIT 1")) {
                produtosOk = true; //Se a query não lançar exceção, a tabela existe
            } catch (SQLException e) {
                System.err.println("ERRO: Tabela 'produtos' não encontrada ou inacessível. " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("ERRO: Falha ao criar Statement para verificar tabelas. " + e.getMessage());
            return false; //Falha geral na verificação
        }
        return animaisOk && produtosOk;
    }

    //Exibe uma mensagem de erro crítica e encerra o programa
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
                //Caso o SwingUtilities falhar (improvável aqui), imprime no console.
                System.err.println("ERRO CRÍTICO ADICIONAL AO MOSTRAR MENSAGEM: " + mensagem);
                e.printStackTrace();
            }
        }
    }
}
