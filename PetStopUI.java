import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

//Importando as classes Pet e PetDAO.
import com.petstop.model.Pet;
import com.petstop.dao.PetDAO;

public class PetStopUI extends JFrame {

    private PetDAO petDAO;

    //Componentes da UI
    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtEspecie;
    private JTextField txtIdade;
    private JCheckBox chkVacinado;

    private JButton btnSalvar;
    private JButton btnAtualizar;
    private JButton btnRemover;
    private JButton btnLimpar;

    private JTable tabelaPets;
    private DefaultTableModel tableModel;

    public PetStopUI() {
        petDAO = new PetDAO(); //Instancia o DAO

        setTitle("PetStop 🐶🐱 Gerenciamento de Animais");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); //Centraliza na tela
        setLayout(new BorderLayout(10, 10)); //Layout principal com espaçamento

        initComponents();
        layoutComponents();
        addListeners();

        //Carrega os pets existentes na tabela ao iniciar
        carregarPetsNaTabela();
    }

    private void initComponents() {
        //Campos de texto e CheckBox
        txtId = new JTextField(5);
        txtId.setEditable(false); //ID não é editável pelo usuário
        txtNome = new JTextField(20);
        txtEspecie = new JTextField(15);
        txtIdade = new JTextField(5);
        chkVacinado = new JCheckBox("Vacinado");

        //Botões
        btnSalvar = new JButton("Salvar Novo");
        btnAtualizar = new JButton("Atualizar Selecionado");
        btnRemover = new JButton("Remover Selecionado");
        btnLimpar = new JButton("Limpar Campos");

        //Tabela
        String[] colunas = {"ID", "Nome", "Espécie", "Idade", "Vacinado?"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //Torna a tabela não editável diretamente
            }
            //Para exibir "Sim"/"Não" em vez de true/false na coluna "Vacinado?"
             @Override
            public Object getValueAt(int row, int column) {
                //Coluna "Vacinado?"
                if (column == 4) {
                    return (Boolean) super.getValueAt(row, column) ? "Sim" : "Não";
                }
                return super.getValueAt(row, column);
            }
        };
        tabelaPets = new JTable(tableModel);
        tabelaPets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //Permite selecionar apenas uma linha
        tabelaPets.getTableHeader().setReorderingAllowed(false); //Impede reordenação de colunas
    }

    private void layoutComponents() {
        //Painel para o formulário de entrada
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Animal"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); //Espaçamento entre componentes
        gbc.anchor = GridBagConstraints.WEST;

        //Linha 0: ID
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painelFormulario.add(txtId, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0; //Reset

        //Linha 1: Nome
        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtNome, gbc);
        gbc.fill = GridBagConstraints.NONE;

        //Linha 2: Espécie
        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Espécie:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtEspecie, gbc);
        gbc.fill = GridBagConstraints.NONE;

        //Linha 3: Idade e Vacinado
        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("Idade:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.NONE; //Não esticar campo idade
        painelFormulario.add(txtIdade, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(chkVacinado, gbc);
        gbc.anchor = GridBagConstraints.WEST; //Reset anchor

        //Painel para os botões de ação
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);

        //Adiciona o painel do formulário e o painel de botões a um painel superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        //Adiciona os painéis principais ao JFrame
        add(painelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tabelaPets), BorderLayout.CENTER); //Tabela com scroll
    }

    private void addListeners() {
        //Botão Salvar
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarPet();
            }
        });

        //Botão Atualizar
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarPet();
            }
        });

        //Botão Remover
        btnRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerPet();
            }
        });

        //Botão Limpar
        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });

        //Listener para seleção de linha na tabela
        tabelaPets.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaPets.getSelectedRow();
                if (linhaSelecionada != -1) { //Verifica se uma linha foi realmente selecionada
                    carregarDadosDoPetSelecionado(linhaSelecionada);
                }
            }
        });
    }

    private void carregarPetsNaTabela() {
        //Limpa a tabela antes de carregar novos dados
        tableModel.setRowCount(0);

        List<Pet> pets = petDAO.listarTodosPets();
        if (pets.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum pet cadastrado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Pet pet : pets) {
                tableModel.addRow(new Object[]{
                        pet.getId(),
                        pet.getNome(),
                        pet.getEspecie(),
                        pet.getIdade(),
                        pet.isVacinado() // OgetValueAt da tableModel cuidará de mostrar "Sim"/"Não"
                });
            }
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtEspecie.setText("");
        txtIdade.setText("");
        chkVacinado.setSelected(false);
        tabelaPets.clearSelection(); //Limpa a seleção da tabela
        txtNome.requestFocus(); //Foca no campo nome
    }

    private void carregarDadosDoPetSelecionado(int linha) {
        if (linha < 0 || linha >= tableModel.getRowCount()) return;

        txtId.setText(tableModel.getValueAt(linha, 0).toString());
        txtNome.setText(tableModel.getValueAt(linha, 1).toString());
        txtEspecie.setText(tableModel.getValueAt(linha, 2).toString());
        txtIdade.setText(tableModel.getValueAt(linha, 3).toString());
        
        String vacinadoStr = tableModel.getValueAt(linha, 4).toString();
        chkVacinado.setSelected("Sim".equalsIgnoreCase(vacinadoStr));
    }

    private void salvarPet() {
        String nome = txtNome.getText().trim();
        String especie = txtEspecie.getText().trim();
        String idadeStr = txtIdade.getText().trim();

        if (nome.isEmpty() || especie.isEmpty() || idadeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios (Nome, Espécie, Idade).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idade;
        try {
            idade = Integer.parseInt(idadeStr);
            if (idade < 0) {
                JOptionPane.showMessageDialog(this, "A idade não pode ser negativa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Idade inválida. Por favor, insira um número.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean vacinado = chkVacinado.isSelected();

        Pet novoPet = new Pet(nome, especie, idade, vacinado);
        if (petDAO.adicionarPet(novoPet)) {
            JOptionPane.showMessageDialog(this, "Pet salvo com sucesso! ID: " + novoPet.getId(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarPetsNaTabela();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o pet. Verifique o console para mais detalhes.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarPet() {
        String idStr = txtId.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum pet selecionado para atualizar. Selecione um pet na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = txtNome.getText().trim();
        String especie = txtEspecie.getText().trim();
        String idadeStr = txtIdade.getText().trim();

        if (nome.isEmpty() || especie.isEmpty() || idadeStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios (Nome, Espécie, Idade).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long id;
        int idade;
        try {
            id = Long.parseLong(idStr);
            idade = Integer.parseInt(idadeStr);
            if (idade < 0) {
                JOptionPane.showMessageDialog(this, "A idade não pode ser negativa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID ou Idade inválida. Verifique os valores.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean vacinado = chkVacinado.isSelected();

        Pet petAtualizado = new Pet(id, nome, especie, idade, vacinado);
        if (petDAO.atualizarPet(petAtualizado)) {
            JOptionPane.showMessageDialog(this, "Pet atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarPetsNaTabela();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar o pet. Verifique o console para mais detalhes.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerPet() {
        String idStr = txtId.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum pet selecionado para remover. Selecione um pet na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover o pet selecionado?\nNome: " + txtNome.getText(),
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                long id = Long.parseLong(idStr);
                if (petDAO.removerPet(id)) {
                    JOptionPane.showMessageDialog(this, "Pet removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarPetsNaTabela();
                    limparCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao remover o pet. Verifique o console para mais detalhes.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido para remoção.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
