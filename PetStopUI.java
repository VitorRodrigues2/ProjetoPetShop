import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.math.BigDecimal;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

//Importar as classes Pet e PetDAO caso necessário.
//import com.petstop.model.Pet;
//import com.petstop.dao.PetDAO;

public class PetStopUI extends JFrame {
    private PetDAO petDAO;
    private ProdutoDAO produtoDAO;
    private JTabbedPane tabbedPane;

    //Componentes da UI de Animais
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

    //Componentes da UI de Produtos
    private JTextField txtIdProduto;
    private JTextField txtNomeProduto;
    private JTextField txtQuantidade;
    private JTextField txtPrecoProduto;
    private JCheckBox chkDisponivelProduto;

    private JButton btnSalvarProduto;
    private JButton btnAtualizarProduto;
    private JButton btnRemoverProduto;
    private JButton btnLimparProduto;

    private JTable tabelaProdutos;
    private DefaultTableModel tableModelProdutos;

    public PetStopUI() {
        petDAO = new PetDAO(); //Instancia o DAO
        produtoDAO = new ProdutoDAO();

        setTitle("PetStop 🐶🐱 Gerenciamento de Animais");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); //Centraliza na tela
        setLayout(new BorderLayout(10, 10)); //Layout principal com espaçamento

        initComponents();
        layoutComponents();
        addListeners();
        addListenersProduto();

        carregarPetsNaTabela();
        carregarProdutosNaTabela();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();

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
    
        //Inicializar componentes produtos (exemplo)
        txtIdProduto = new JTextField(5);
        txtIdProduto.setEditable(false);
        txtNomeProduto = new JTextField(20);
        txtQuantidade = new JTextField(15);
        txtPrecoProduto = new JTextField(5);
        chkDisponivelProduto = new JCheckBox("Disponível");

        btnSalvarProduto = new JButton("Salvar Produto");
        btnAtualizarProduto = new JButton("Atualizar Produto");
        btnRemoverProduto = new JButton("Remover Produto");
        btnLimparProduto = new JButton("Limpar Produto");

        String[] colunasProdutos = {"ID", "Nome", "Quantidade", "Preço", "Disponível"};
        tableModelProdutos = new DefaultTableModel(colunasProdutos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Object getValueAt(int row, int column) {
                if (column == 4) {
                    return (Boolean) super.getValueAt(row, column) ? "Sim" : "Não";
                }
                return super.getValueAt(row, column);
            }
        };
        tabelaProdutos = new JTable(tableModelProdutos);
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.getTableHeader().setReorderingAllowed(false);
    }

    private void layoutComponents() {
        // Painel para o formulário de entrada dos dados do animal
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Animal"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        painelFormulario.add(txtId, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtNome, gbc);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 2;
        painelFormulario.add(new JLabel("Espécie:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        painelFormulario.add(txtEspecie, gbc);
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridx = 0; gbc.gridy = 3;
        painelFormulario.add(new JLabel("Idade:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.NONE;
        painelFormulario.add(txtIdade, gbc);

        gbc.gridx = 2; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        painelFormulario.add(chkVacinado, gbc);
        gbc.anchor = GridBagConstraints.WEST;

        // Painel de botões para dados do animal
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnLimpar);

        // Painel superior com formulário + botões
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelFormulario, BorderLayout.CENTER);
        painelSuperior.add(painelBotoes, BorderLayout.SOUTH);

        // Painel da aba "Dados do Animal" com formulário e tabela
        JPanel abaDadosAnimal = new JPanel(new BorderLayout());
        abaDadosAnimal.add(painelSuperior, BorderLayout.NORTH);
        abaDadosAnimal.add(new JScrollPane(tabelaPets), BorderLayout.CENTER);


        // Painel formulário produtos
        JPanel painelFormularioProdutos = new JPanel(new GridBagLayout());
        painelFormularioProdutos.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbcProd = new GridBagConstraints();
        gbcProd.insets = new Insets(5,5,5,5);
        gbcProd.anchor = GridBagConstraints.WEST;

        gbcProd.gridx = 0; gbcProd.gridy = 0;
        painelFormularioProdutos.add(new JLabel("ID:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.fill = GridBagConstraints.HORIZONTAL; gbcProd.weightx = 1.0;
        painelFormularioProdutos.add(txtIdProduto, gbcProd);
        gbcProd.fill = GridBagConstraints.NONE; gbcProd.weightx = 0;

        gbcProd.gridx = 0; gbcProd.gridy = 1;
        painelFormularioProdutos.add(new JLabel("Nome:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.fill = GridBagConstraints.HORIZONTAL;
        painelFormularioProdutos.add(txtNomeProduto, gbcProd);
        gbcProd.fill = GridBagConstraints.NONE;

        gbcProd.gridx = 0; gbcProd.gridy = 2;
        painelFormularioProdutos.add(new JLabel("Quantidade:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.fill = GridBagConstraints.HORIZONTAL;
        painelFormularioProdutos.add(txtQuantidade, gbcProd);
        gbcProd.fill = GridBagConstraints.NONE;

        gbcProd.gridx = 0; gbcProd.gridy = 3;
        painelFormularioProdutos.add(new JLabel("Preço:"), gbcProd);
        gbcProd.gridx = 1; gbcProd.fill = GridBagConstraints.HORIZONTAL;
        painelFormularioProdutos.add(txtPrecoProduto, gbcProd);
        gbcProd.fill = GridBagConstraints.NONE;

        gbcProd.gridx = 2; gbcProd.gridy = 3; gbcProd.anchor = GridBagConstraints.EAST;
        painelFormularioProdutos.add(chkDisponivelProduto, gbcProd);
        gbcProd.anchor = GridBagConstraints.WEST;

        // Painel botões produtos
        JPanel painelBotoesProdutos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelBotoesProdutos.add(btnSalvarProduto);
        painelBotoesProdutos.add(btnAtualizarProduto);
        painelBotoesProdutos.add(btnRemoverProduto);
        painelBotoesProdutos.add(btnLimparProduto);

        // Painel superior produtos com formulário + botões
        JPanel painelSuperiorProdutos = new JPanel(new BorderLayout());
        painelSuperiorProdutos.add(painelFormularioProdutos, BorderLayout.CENTER);
        painelSuperiorProdutos.add(painelBotoesProdutos, BorderLayout.SOUTH);

        // Painel da aba Produtos com formulário e tabela
        JPanel abaProdutos = new JPanel(new BorderLayout());
        abaProdutos.add(painelSuperiorProdutos, BorderLayout.NORTH);
        abaProdutos.add(new JScrollPane(tabelaProdutos), BorderLayout.CENTER);

        // Adicionar abas ao tabbedPane (já existente)
        tabbedPane.addTab("Dados do Animal", abaDadosAnimal);
        tabbedPane.addTab("Produtos", abaProdutos);

        add(tabbedPane, BorderLayout.CENTER);
    }

    //Dados do Animal
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

    //Dados do Produto
    private void addListenersProduto() {
        //Botão Salvar
        btnSalvarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarProduto();
            }
        });

        //Botão Atualizar
        btnAtualizarProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarProduto();
            }
        });

        //Botão Remover
        btnRemoverProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removerProduto();
            }
        });

        //Botão Limpar
        btnLimparProduto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCamposProduto();
            }
        });

        //Listener para seleção de linha na tabela
        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaProdutos.getSelectedRow();
                if (linhaSelecionada != -1) {
                    carregarDadosDoProdutoSelecionado(linhaSelecionada);
                }
            }
        });
    }

    private void carregarPetsNaTabela() {
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
                        pet.isVacinado()
                });
            }
        }
    }

    private void carregarProdutosNaTabela() {
        tableModelProdutos.setRowCount(0);

        List<Produto> produtos = produtoDAO.listarTodosProdutos();
        if (produtos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum produto cadastrado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Produto produto : produtos) {
                tableModelProdutos.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getQuantidade(),
                    produto.getPreco(),
                    produto.isDisponivel()
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

    private void limparCamposProduto() {
        txtNomeProduto.setText("");
        txtPrecoProduto.setText("");
        txtQuantidade.setText("");
        chkDisponivelProduto.setSelected(false);
        tabelaProdutos.clearSelection(); //Limpa a seleção da tabela
        txtNomeProduto.requestFocus(); //Foca no campo nome
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

    private void carregarDadosDoProdutoSelecionado(int linha) {
        if (linha < 0 || linha >= tableModelProdutos.getRowCount()) return;

        txtIdProduto.setText(tableModelProdutos.getValueAt(linha, 0).toString());
        txtNomeProduto.setText(tableModelProdutos.getValueAt(linha, 1).toString());
        txtQuantidade.setText(tableModelProdutos.getValueAt(linha, 2).toString());
        txtPrecoProduto.setText(tableModelProdutos.getValueAt(linha, 3).toString());

        String disponivelStr = tableModelProdutos.getValueAt(linha, 4).toString();
        chkDisponivelProduto.setSelected("Sim".equalsIgnoreCase(disponivelStr));
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

    private void salvarProduto() {
        String nome = txtNomeProduto.getText().trim();
        String precoStr = txtPrecoProduto.getText().trim();
        String quantidadeStr = txtQuantidade.getText().trim();
        boolean disponivel = chkDisponivelProduto.isSelected();

        if (nome.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios (Nome, Preço).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BigDecimal preco;
        try {
            preco = new BigDecimal(precoStr);
            if (preco.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "O preço não pode ser negativo.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido. Por favor, insira um número decimal.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeStr);
            if (quantidade < 0) {
                JOptionPane.showMessageDialog(this, "A quantidade não pode ser negativa.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Por favor, insira um número inteiro.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produto novoProduto = new Produto(nome, quantidade, preco, disponivel);
        if (produtoDAO.adicionarProduto(novoProduto)) {
            JOptionPane.showMessageDialog(this, "Produto salvo com sucesso! ID: " + novoProduto.getId(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarProdutosNaTabela();
            limparCamposProduto();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao salvar o produto. Verifique o console para mais detalhes.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
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

    private void atualizarProduto() {
        String idStr = txtIdProduto.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum produto selecionado para atualizar. Selecione um produto na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nome = txtNomeProduto.getText().trim();
        String quantidadeStr = txtQuantidade.getText().trim();
        String precoStr = txtPrecoProduto.getText().trim();

        if (nome.isEmpty() || quantidadeStr.isEmpty() || precoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios (Nome, Quantidade, Preço).", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long id;
        int quantidade;
        BigDecimal preco;

        try {
            id = Long.parseLong(idStr);
            quantidade = Integer.parseInt(quantidadeStr);
            preco = new BigDecimal(precoStr.replace(",", "."));

            if (quantidade < 0 || preco.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Quantidade e Preço não podem ser negativos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID, Quantidade ou Preço inválido. Verifique os valores.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean disponivel = chkDisponivelProduto.isSelected();

        Produto produtoAtualizado = new Produto(id, nome, quantidade, preco, disponivel);
        if (produtoDAO.atualizarProduto(produtoAtualizado)) {
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarProdutosNaTabela();
            limparCamposProduto();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar o produto. Verifique o console para mais detalhes.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
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

    private void removerProduto() {
        String idStr = txtIdProduto.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum produto selecionado para remover. Selecione um produto na tabela.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover o produto selecionado?\nNome: " + txtNomeProduto.getText(),
                "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                long id = Long.parseLong(idStr);
                if (produtoDAO.removerProduto(id)) {
                    JOptionPane.showMessageDialog(this, "Produto removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    carregarProdutosNaTabela();
                    limparCamposProduto();
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao remover o produto. Verifique o console para mais detalhes.", "Erro no Banco", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido para remoção.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
