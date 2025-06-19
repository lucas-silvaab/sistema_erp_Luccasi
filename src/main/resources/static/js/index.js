
        async function carregarInfoUsuario() {
    const welcomeElement = document.getElementById('welcome-message');
    try {
        const response = await fetch('/usuario/info');
        if (!response.ok) {
            // Se falhar, pode ser que a sessão expirou, redireciona para o login
            window.location.href = '/login.html'; // Redireciona para login.html
            return;
        }
        const usuario = await response.json();
        // Atualiza o texto com o nome do usuário retornado pela API
        welcomeElement.textContent = `Olá, ${usuario.nome}!`;

    } catch (error) {
        window.location.href = '/login.html'; // Redireciona para login.html em caso de erro
            return;
        console.error('Erro ao buscar info do usuário:', error);
        welcomeElement.textContent = 'Não foi possível carregar os dados.';
    }
}
        // Função para carregar e exibir os usuários na tabela
        async function carregarUsuarios() {
            const tabelaCorpo = document.getElementById('tabela-usuarios-corpo');
            tabelaCorpo.innerHTML = '<tr><td colspan="4" class="text-center">Carregando...</td></tr>'; 

            try {
                const response = await fetch('/usuario/todos');
                if (!response.ok) throw new Error('Erro ao buscar dados.');
                const usuarios = await response.json();
                tabelaCorpo.innerHTML = '';

                if (usuarios.length === 0) {
                    tabelaCorpo.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum usuário cadastrado.</td></tr>';
                    return;
                }

                usuarios.forEach(usuario => {
                    const linha = `
                        <tr>
                            <td>${usuario.id}</td>
                            <td>${usuario.nome}</td>
                            <td>${usuario.email}</td>
                            <td class="text-center">
                                <button class="btn btn-primary btn-sm btn-edit" data-user-id="${usuario.id}" data-user-nome="${usuario.nome}" data-user-email="${usuario.email}" title="Editar">Editar</button>
                                <button class="btn btn-danger btn-sm btn-delete" data-user-email="${usuario.email}" title="Deletar">Deletar</button>
                            </td>
                        </tr>
                    `;
                    tabelaCorpo.innerHTML += linha;
                });

            } catch (error) {
                console.error('Falha ao carregar usuários:', error);
                tabelaCorpo.innerHTML = '<tr><td colspan="4" class="text-center">Erro ao carregar os dados.</td></tr>';
            }
        }

        // Função para deletar um usuário
        async function deletarUsuario(email, botaoClicado) {
            if (!confirm(`Tem certeza que deseja deletar o usuário com o email ${email}?`)) return;
            try {
                const response = await fetch(`/usuario?email=${encodeURIComponent(email)}`, { method: 'DELETE' });
                if (response.ok) {
                    alert('Usuário deletado com sucesso!');
                    botaoClicado.closest('tr').remove();
                } else {
                    throw new Error('Falha ao deletar o usuário.');
                }
            } catch (error) {
                console.error('Erro:', error);
                alert('Ocorreu um erro ao tentar deletar o usuário.');
            }
        }

        // "Ouvinte" principal que roda quando a página é carregada
        document.addEventListener('DOMContentLoaded', () => {
            carregarUsuarios();
            carregarInfoUsuario();

            const tabelaCorpo = document.getElementById('tabela-usuarios-corpo');
            const modalEdicaoElement = document.getElementById('modalEdicao');
            const modalEdicao = new bootstrap.Modal(modalEdicaoElement);
            const formEdicao = document.getElementById('formEdicao');

            const formCadastro = document.getElementById('formCadastro'); // Pega o novo formulário de cadastro
            const modalCadastroElement = document.getElementById('modalCadastro'); // Pega o novo modal de cadastro
            const modalCadastro = new bootstrap.Modal(modalCadastroElement); // Instancia o modal de cadastro

            // Ouvinte de cliques na tabela para DELETAR ou ABRIR MODAL DE EDIÇÃO
            tabelaCorpo.addEventListener('click', (event) => {
                const target = event.target;
                if (target.classList.contains('btn-delete')) {
                    const emailParaDeletar = target.dataset.userEmail;
                    deletarUsuario(emailParaDeletar, target);
                } 
                // NOVA LÓGICA: Se o botão de editar for clicado
                else if (target.classList.contains('btn-edit')) {
                    // 1. Pega os dados do usuário guardados nos atributos data-* do botão
                    const id = target.dataset.userId;
                    const nome = target.dataset.userNome;
                    const email = target.dataset.userEmail;
                    
                    // 2. Preenche os campos do formulário no modal com esses dados
                    document.getElementById('editUserId').value = id;
                    document.getElementById('editUserName').value = nome;
                    document.getElementById('editUserEmail').value = email;

                    // 3. Abre o modal de edição
                    modalEdicao.show();
                }
            });

            // NOVO OUVINTE: Para o envio do formulário de edição
            formEdicao.addEventListener('submit', async (event) => {
                event.preventDefault(); // Impede o recarregamento da página

                // Pega os dados (possivelmente alterados) do formulário
                const id = document.getElementById('editUserId').value;
                const nome = document.getElementById('editUserName').value;
                const email = document.getElementById('editUserEmail').value;
                
                const usuarioAtualizado = { nome, email };

                try {
                    // Chama a API com o método PUT, passando o ID na URL e os dados no corpo
                    const response = await fetch(`/usuario?id=${id}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify(usuarioAtualizado)
                    });

                    if (response.ok) {
                        alert('Usuário atualizado com sucesso!');
                        modalEdicao.hide(); // Fecha o modal
                        carregarUsuarios(); // Recarrega a lista para mostrar os dados atualizados
                    } else {
                        throw new Error('Falha ao atualizar o usuário.');
                    }
                } catch (error) {
                    console.error('Erro ao atualizar:', error);
                    alert('Ocorreu um erro ao tentar atualizar o usuário.');
                }
            });

            // Adiciona o ouvinte para o formulário de cadastro (agora na index.html)
            formCadastro.addEventListener('submit', async (event) => {
                event.preventDefault();

                const nome = document.getElementById('nomeCadastro').value;
                const email = document.getElementById('emailCadastro').value;
                const senha = document.getElementById('senhaCadastro').value;

                const dadosUsuario = {
                    nome: nome,
                    email: email,
                    senha: senha
                };

                console.log('Enviando para a API:', dadosUsuario);

                try {
                    const response = await fetch('/usuario', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(dadosUsuario)
                    });

                    if (response.ok) {
                        alert('Usuário cadastrado com sucesso!');
                        formCadastro.reset();
                        modalCadastro.hide(); // Fecha o modal de cadastro
                        carregarUsuarios(); // Recarrega a lista de usuários
                    } else {
                        alert('Erro ao cadastrar usuário. Verifique os dados ou tente novamente.');
                    }
                } catch (error) {
                    console.error('Erro na requisição:', error);
                    alert('Não foi possível se conectar ao servidor.');
                }
            });
        });
    