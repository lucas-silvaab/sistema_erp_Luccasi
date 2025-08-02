document.addEventListener('DOMContentLoaded', () => {
    const tabelaCorpo = document.getElementById('tabela-produtos-corpo');
    if (!tabelaCorpo) { return; }

    const API_BASE_URL = 'http://localhost:8080/api';
    const formProdutoBase = document.getElementById('formProdutoBase');
    const formVariante = document.getElementById('formVariante');
    
    const modalProdutoBase = new bootstrap.Modal(document.getElementById('modalProdutoBase'));
    const modalVariante = new bootstrap.Modal(document.getElementById('modalVariante'));
    const modalDetalhes = new bootstrap.Modal(document.getElementById('modalDetalhesVariante'));

    const carregarProdutos = async () => {
        try {
            const response = await fetch(`${API_BASE_URL}/produtos-base`);
            if (!response.ok) throw new Error('Falha ao buscar produtos.');
            const produtosBase = await response.json();
            
            tabelaCorpo.innerHTML = '';
            if (produtosBase.length === 0) {
                tabelaCorpo.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum produto cadastrado.</td></tr>';
                return;
            }

            produtosBase.forEach(pb => {
                const totalLinhas = (pb.variantes && pb.variantes.length > 0) ? pb.variantes.length : 1;
                const tr = document.createElement('tr');
                
                let primeiraLinhaHTML = `<td rowspan="${totalLinhas}" class="align-middle">${pb.nome} (${pb.codigoBase})</td>`;
                
                // MUDANÇA AQUI: Passamos o pb.codigoBase para a função onclick
                const acoesHTML = `
                    <td class="text-center align-middle" rowspan="${totalLinhas}">
                        <button class="btn btn-sm btn-info" 
                                onclick="abrirModalVariante(${pb.id}, '${pb.nome}', '${pb.codigoBase}')" 
                                title="Adicionar nova variante">
                            <i class="bi bi-plus-circle-fill"></i>
                        </button>
                    </td>`;

                if (pb.variantes && pb.variantes.length > 0) {
                    const primeiraVariante = pb.variantes[0];
                    primeiraLinhaHTML += `<td>${primeiraVariante.sku}</td><td class="text-center"><button class="btn btn-sm btn-outline-secondary" onclick="abrirModalDetalhes(${primeiraVariante.id})"><i class="bi bi-eye-fill"></i></button></td>`;
                    tr.innerHTML = primeiraLinhaHTML + acoesHTML;
                    tabelaCorpo.appendChild(tr);

                    for (let i = 1; i < pb.variantes.length; i++) {
                        const variante = pb.variantes[i];
                        const trVarianteAdicional = document.createElement('tr');
                        trVarianteAdicional.innerHTML = `<td>${variante.sku}</td><td class="text-center"><button class="btn btn-sm btn-outline-secondary" onclick="abrirModalDetalhes(${variante.id})"><i class="bi bi-eye-fill"></i></button></td>`;
                        tabelaCorpo.appendChild(trVarianteAdicional);
                    }
                } else {
                    primeiraLinhaHTML += `<td colspan="2" class="text-center text-muted align-middle">Nenhuma variante cadastrada</td>`;
                    tr.innerHTML = primeiraLinhaHTML + acoesHTML;
                    tabelaCorpo.appendChild(tr);
                }
            });
        } catch (error) {
            console.error('Erro ao carregar produtos:', error);
            tabelaCorpo.innerHTML = '<tr><td colspan="4" class="text-center text-danger">Erro ao carregar dados.</td></tr>';
        }
    };
    
    formProdutoBase.addEventListener('submit', async (e) => {
        e.preventDefault();
        const payload = { nome: document.getElementById('nome').value, codigoBase: document.getElementById('codigoBase').value, categoria: document.getElementById('categoria').value };
        try {
            const response = await fetch(`${API_BASE_URL}/produtos-base`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
            if (!response.ok) throw new Error('Falha ao criar produto base.');
            formProdutoBase.reset();
            modalProdutoBase.hide();
            carregarProdutos();
        } catch (error) { console.error('Erro:', error); alert('Não foi possível salvar o Produto Base.'); }
    });
    
    // MUDANÇA AQUI: A função agora recebe o codigoBase
    window.abrirModalVariante = async (produtoBaseId, produtoBaseNome, codigoBase) => {
        document.getElementById('modalVarianteLabel').textContent = `Cadastrar Variante para: ${produtoBaseNome}`;
        document.getElementById('varianteProdutoBaseId').value = produtoBaseId;
        
        // MUDANÇA AQUI: Preenchemos o campo SKU e colocamos o foco nele
        const skuInput = document.getElementById('sku');
        skuInput.value = codigoBase + '.';
        
        const opcoesContainer = document.getElementById('opcoes-container-modal');
        opcoesContainer.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"></div></div>';
        try {
            const response = await fetch(`${API_BASE_URL}/opcoes`);
            if (!response.ok) throw new Error('Falha ao buscar opções.');
            const opcoes = await response.json();
            opcoesContainer.innerHTML = '';
            opcoes.forEach(opcao => {
                let checkboxesHTML = '<div class="d-flex flex-wrap gap-3">';
                if (opcao.valores && opcao.valores.length > 0) {
                    opcao.valores.forEach(valor => {
                        checkboxesHTML += `<div class="form-check"><input class="form-check-input" type="checkbox" name="valoresOpcaoIds" value="${valor.id}" id="valor-${valor.id}"><label class="form-check-label" for="valor-${valor.id}">${valor.valor}</label></div>`;
                    });
                }
                checkboxesHTML += '</div>';
                opcoesContainer.innerHTML += `<div class="mb-3"><p class="fw-bold mb-1">${opcao.nome}</p>${checkboxesHTML}</div>`;
            });
            modalVariante.show();
            
            // MUDANÇA AQUI: Força o foco no input do SKU depois que o modal estiver visível
            setTimeout(() => skuInput.focus(), 500);

        } catch (error) { console.error('Erro:', error); opcoesContainer.innerHTML = '<p class="text-danger">Não foi possível carregar as opções.</p>'; modalVariante.show(); }
    };
    
    window.abrirModalDetalhes = async (varianteId) => {
        const modalCorpo = document.getElementById('modalDetalhesCorpo');
        const modalTitulo = document.getElementById('modalDetalhesLabel');
        modalTitulo.textContent = 'Carregando Detalhes...';
        modalCorpo.innerHTML = '<div class="text-center"><div class="spinner-border" role="status"></div></div>';
        modalDetalhes.show();
        try {
            const response = await fetch(`${API_BASE_URL}/variantes/${varianteId}`);
            if (!response.ok) throw new Error('Variante não encontrada.');
            const variante = await response.json();
            modalTitulo.textContent = `Detalhes da Variante: ${variante.sku}`;
            let detalhesHTML = `<dl class="row"><dt class="col-sm-4">Produto Base:</dt><dd class="col-sm-8">${variante.produtoBase.nome}</dd><dt class="col-sm-4">SKU:</dt><dd class="col-sm-8">${variante.sku}</dd></dl><hr><h6>Configuração Específica:</h6><ul>`;
            if (variante.configuracao && variante.configuracao.length > 0) {
                variante.configuracao.forEach(config => { detalhesHTML += `<li><strong>${config.opcaoNome}:</strong> ${config.valor}</li>`; });
            } else { detalhesHTML += '<li>Nenhuma configuração específica.</li>'; }
            detalhesHTML += '</ul>';
            modalCorpo.innerHTML = detalhesHTML;
        } catch (error) { console.error("Erro:", error); modalCorpo.innerHTML = `<p class="text-danger">Não foi possível carregar os detalhes. ${error.message}</p>`; }
    };
     
    formVariante.addEventListener('submit', async (e) => {
        e.preventDefault();
        const produtoBaseId = document.getElementById('varianteProdutoBaseId').value;
        const selectedCheckboxes = document.querySelectorAll('#opcoes-container-modal input[name="valoresOpcaoIds"]:checked');
        const valoresOpcaoIds = Array.from(selectedCheckboxes).map(cb => parseInt(cb.value));
        const payload = { sku: document.getElementById('sku').value, valoresOpcaoIds: valoresOpcaoIds };
        try {
            const response = await fetch(`${API_BASE_URL}/produtos-base/${produtoBaseId}/variantes`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
            if (!response.ok) throw new Error('Falha ao criar variante.');
            formVariante.reset();
            modalVariante.hide();
            carregarProdutos();
        } catch (error) { console.error('Erro:', error); alert('Não foi possível salvar a Variante.'); }
    });

    carregarProdutos();
});