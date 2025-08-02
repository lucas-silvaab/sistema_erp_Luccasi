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
        tabelaCorpo.innerHTML = '<tr><td colspan="4" class="text-center text-danger">Erro ao carregar os dados.</td></tr>';
    }
}

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

document.addEventListener('DOMContentLoaded', () => {
    // Agora só chama a função específica desta página
    carregarUsuarios();

    const tabelaCorpo = document.getElementById('tabela-usuarios-corpo');
    const modalEdicao = new bootstrap.Modal(document.getElementById('modalEdicao'));
    const formEdicao = document.getElementById('formEdicao');
    const modalCadastro = new bootstrap.Modal(document.getElementById('modalCadastro'));
    const formCadastro = document.getElementById('formCadastro');

    tabelaCorpo.addEventListener('click', (event) => {
        const target = event.target;
        if (target.classList.contains('btn-delete')) {
            deletarUsuario(target.dataset.userEmail, target);
        } else if (target.classList.contains('btn-edit')) {
            document.getElementById('editUserId').value = target.dataset.userId;
            document.getElementById('editUserName').value = target.dataset.userNome;
            document.getElementById('editUserEmail').value = target.dataset.userEmail;
            modalEdicao.show();
        }
    });

    formEdicao.addEventListener('submit', async (event) => {
        event.preventDefault();
        const id = document.getElementById('editUserId').value;
        const nome = document.getElementById('editUserName').value;
        const email = document.getElementById('editUserEmail').value;
        const usuarioAtualizado = { nome, email };
        try {
            const response = await fetch(`/usuario?id=${id}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(usuarioAtualizado) });
            if (response.ok) {
                alert('Usuário atualizado com sucesso!');
                modalEdicao.hide();
                carregarUsuarios();
            } else { throw new Error('Falha ao atualizar o usuário.'); }
        } catch (error) { console.error('Erro:', error); alert('Ocorreu um erro ao tentar atualizar.'); }
    });

    formCadastro.addEventListener('submit', async (event) => {
        event.preventDefault();
        const dadosUsuario = { nome: document.getElementById('nomeCadastro').value, email: document.getElementById('emailCadastro').value, senha: document.getElementById('senhaCadastro').value };
        try {
            const response = await fetch('/usuario', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(dadosUsuario) });
            if (response.ok) {
                alert('Usuário cadastrado com sucesso!');
                formCadastro.reset();
                modalCadastro.hide();
                carregarUsuarios();
            } else { alert('Erro ao cadastrar usuário.'); }
        } catch (error) { console.error('Erro:', error); alert('Não foi possível se conectar ao servidor.'); }
    });
});