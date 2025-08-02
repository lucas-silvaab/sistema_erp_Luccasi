document.addEventListener('DOMContentLoaded', () => {
    const listaOpcoesContainer = document.getElementById('lista-opcoes');

    // ---- VERIFICAÇÃO IMPORTANTE ----
    // Se este elemento principal da página de configurações não for encontrado,
    // o script para imediatamente. Isso resolve o erro "is null".
    if (!listaOpcoesContainer) {
        return;
    }

    // O resto do código só executa se estivermos na página correta.
    const API_BASE_URL = 'http://localhost:8080/api';
    const modalNovaOpcao = new bootstrap.Modal(document.getElementById('modalNovaOpcao'));
    const modalNovoValor = new bootstrap.Modal(document.getElementById('modalNovoValor'));
    const formNovaOpcao = document.getElementById('formNovaOpcao');
    const formNovoValor = document.getElementById('formNovoValor');

    const carregarOpcoes = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/opcoes`);
            if (!response.ok) throw new Error('Falha ao buscar opções.');
            const opcoes = await response.json();

            listaOpcoesContainer.innerHTML = '';
            if (opcoes.length === 0) {
                listaOpcoesContainer.innerHTML = '<p class="text-center">Nenhuma opção de configuração cadastrada.</p>';
                return;
            }

            opcoes.forEach(opcao => {
                let valoresHTML = '';
                if (opcao.valores && opcao.valores.length > 0) {
                    opcao.valores.forEach(valor => {
                        valoresHTML += `<li class="list-group-item">${valor.valor}</li>`;
                    });
                } else {
                    valoresHTML = '<li class="list-group-item text-muted">Nenhum valor cadastrado.</li>';
                }

                const cardHTML = `
                    <div class="col-md-6 col-lg-4">
                        <div class="card h-100">
                            <div class="card-header fw-bold fs-5">${opcao.nome}</div>
                            <div class="card-body">
                                <ul class="list-group list-group-flush">
                                    ${valoresHTML}
                                </ul>
                            </div>
                            <div class="card-footer text-center">
                                <button class="btn btn-sm btn-primary" onclick="abrirModalAdicionarValor(${opcao.id}, '${opcao.nome}')">
                                    Adicionar Valor
                                </button>
                            </div>
                        </div>
                    </div>
                `;
                listaOpcoesContainer.innerHTML += cardHTML;
            });
        } catch (error) {
            console.error('Erro:', error);
            listaOpcoesContainer.innerHTML = '<p class="text-danger text-center">Erro ao carregar configurações.</p>';
        }
    };

    formNovaOpcao.addEventListener('submit', async (e) => {
        e.preventDefault();
        const nome = document.getElementById('nomeNovaOpcao').value;
        try {
            const response = await fetch(`${API_BASE_URL}/opcoes`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nome: nome })
            });
            if (!response.ok) throw new Error('Falha ao salvar opção.');
            formNovaOpcao.reset();
            modalNovaOpcao.hide();
            carregarOpcoes();
        } catch (error) {
            console.error('Erro:', error);
            alert('Não foi possível criar a nova opção.');
        }
    });

    window.abrirModalAdicionarValor = (opcaoId, opcaoNome) => {
        document.getElementById('idOpcaoPai').value = opcaoId;
        document.getElementById('modalNovoValorLabel').textContent = `Adicionar Valor para: ${opcaoNome}`;
        modalNovoValor.show();
    };

    formNovoValor.addEventListener('submit', async (e) => {
        e.preventDefault();
        const opcaoId = document.getElementById('idOpcaoPai').value;
        const valor = document.getElementById('nomeNovoValor').value;
        try {
            const response = await fetch(`${API_BASE_URL}/opcoes/${opcaoId}/valores`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ valor: valor })
            });
            if (!response.ok) throw new Error('Falha ao salvar valor.');
            formNovoValor.reset();
            modalNovoValor.hide();
            carregarOpcoes();
        } catch (error) {
            console.error('Erro:', error);
            alert('Não foi possível adicionar o novo valor.');
        }
    });

    carregarOpcoes();
});