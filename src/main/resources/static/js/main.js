// Função para carregar as informações do usuário logado na sidebar
async function carregarInfoUsuario() {
    const welcomeElement = document.getElementById('welcome-message');
    // Se o elemento não existir na página, não faz nada
    if (!welcomeElement) return;

    try {
        const response = await fetch('/usuario/info');
        if (!response.ok) {
            // Se a sessão expirou, o Spring Security redireciona para o login
            // e a verificação de 'ok' falha, então não precisamos redirecionar aqui.
            throw new Error('Sessão expirada ou usuário não autenticado.');
        }
        const usuario = await response.json();
        welcomeElement.textContent = `Olá, ${usuario.nome}!`;
    } catch (error) {
        console.error('Erro ao buscar info do usuário:', error);
        // Em caso de erro, remove a mensagem de "Carregando..."
        welcomeElement.textContent = 'Visitante';
    }
}

// Lógica para controlar o botão de expandir/recolher a sidebar
function inicializarSidebar() {
    const sidebarToggle = document.getElementById('sidebar-toggle');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function () {
            document.body.classList.toggle('collapsed');
        });
    }
}

// Evento principal que roda em TODAS as páginas
document.addEventListener('DOMContentLoaded', () => {
    inicializarSidebar();
    carregarInfoUsuario();
});