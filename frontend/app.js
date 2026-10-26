const API_BASE_URL = 'http://localhost:8080/api/v1';

const state = {
    category: 'all',
    language: 'pt',
    timeframe: 'week',
    page: 1,
    pageSize: 15,
    hasMore: false,
    jwtToken: localStorage.getItem('nexus_jwt') || null
};

const i18n = {
    pt: {
        loginBtn: 'Entrar / Admin',
        heroBadge: 'Feed Inteligente & Newsletter Diária',
        heroTitle: 'O pulso do mundo tech direto no seu e-mail.',
        heroSub: 'Acompanhe em tempo real ou assine nosso digest matinal com curadoria automatizada sobre código, games, cibersegurança e política.',
        subscribeBtn: 'Assinar Agora',
        catAll: '⚡ Todas',
        catTech: 'Tecnologia',
        catGames: 'Games',
        catCyber: 'Cibersegurança',
        catPolitics: 'Política',
        timeframeWeek: 'Janela: Últimos 7 dias',
        timeframeMonth: 'Janela: Últimos 30 dias',
        loadMorePage: 'Carregar mais notícias',
        expandToMonth: 'Fim da semana • Buscar no último mês',
        readArticle: 'Ler matéria completa →',
        placeholderName: 'Seu nome',
        placeholderEmail: 'seu@email.com'
    },
    en: {
        loginBtn: 'Login / Admin',
        heroBadge: 'Smart Feed & Daily Newsletter',
        heroTitle: 'The pulse of the tech world straight to your inbox.',
        heroSub: 'Follow in real time or subscribe to our automated morning digest covering code, gaming, cybersecurity, and politics.',
        subscribeBtn: 'Subscribe Now',
        catAll: 'All News',
        catTech: 'Technology',
        catGames: 'Gaming',
        catCyber: 'Cybersecurity',
        catPolitics: 'Politics',
        timeframeWeek: 'Window: Last 7 days',
        timeframeMonth: 'Window: Last 30 days',
        loadMorePage: 'Load more news',
        expandToMonth: 'End of weekly news • Expand to last month',
        readArticle: 'Read full story →',
        placeholderName: 'Your name',
        placeholderEmail: 'your@email.com'
    }
};

document.addEventListener('DOMContentLoaded', () => {
    initParticles();
    updateAuthUI();
    fetchNews(true);

    document.getElementById('subscribe-form').addEventListener('submit', handleSubscribe);
    document.getElementById('login-form').addEventListener('submit', handleLogin);
});

/**
 * Inicializa os efeitos animados de fundo com tsParticles
 */
function initParticles() {
    if (!window.tsParticles) return;
    window.tsParticles.load("tsparticles", {
        fpsLimit: 60,
        background: { color: "transparent" },
        particles: {
            number: { value: 45, density: { enable: true, area: 900 } },
            color: { value: ["#8b5cf6", "#6366f1", "#06b6d4"] },
            links: {
                enable: true,
                distance: 145,
                color: "#6366f1",
                opacity: 0.16,
                width: 1
            },
            move: {
                enable: true,
                speed: 0.7,
                direction: "none",
                outModes: { default: "bounce" }
            },
            opacity: { value: { min: 0.15, max: 0.5 } },
            size: { value: { min: 1, max: 2.5 } }
        },
        interactivity: {
            events: {
                onHover: { enable: true, mode: "grab" }
            },
            modes: {
                grab: { distance: 160, links: { opacity: 0.35 } }
            }
        },
        detectRetina: true
    });
}

async function fetchNews(resetGrid = false) {
    const grid = document.getElementById('news-grid');
    const loader = document.getElementById('news-loading');
    const loadMoreBtn = document.getElementById('btn-load-more');

    if (resetGrid) {
        grid.innerHTML = '';
        loader.classList.remove('hidden');
        loadMoreBtn.classList.add('hidden');
    }

    try {
        const url = `${API_BASE_URL}/news?category=${state.category}&language=${state.language}&timeframe=${state.timeframe}&page=${state.page}&pageSize=${state.pageSize}`;
        console.log('[NexusNews Fetch]:', url);

        const response = await fetch(url);
        if (!response.ok) throw new Error(`Status HTTP: ${response.status}`);

        const data = await response.json();
        console.log('[NexusNews Resposta]:', data);

        state.hasMore = data.hasMore;
        renderArticles(data.articles, resetGrid);
        updatePaginationButton();
    } catch (error) {
        console.error('Erro ao buscar notícias:', error);
        grid.innerHTML = `
            <div class="col-span-full rounded-2xl border border-red-500/30 bg-red-500/10 p-6 text-center text-sm text-red-300">
                Erro ao conectar com <b>${API_BASE_URL}/news</b>. Verifique o console (F12) e se o backend está ativo.
            </div>`;
    } finally {
        loader.classList.add('hidden');
    }
}

function renderArticles(articles, resetGrid) {
    const grid = document.getElementById('news-grid');

    if (!articles || articles.length === 0) {
        if (resetGrid) {
            grid.innerHTML = `
                <div class="col-span-full rounded-2xl border border-white/10 bg-zinc-900/40 p-12 text-center">
                    <p class="text-zinc-400 text-sm">Nenhuma notícia retornada pela API para o idioma <b>${state.language.toUpperCase()}</b> na categoria <b>${state.category}</b>.</p>
                </div>`;
        }
        return;
    }

    const html = articles.map(article => {
        const dateFormatted = article.publishedAt
            ? new Date(article.publishedAt).toLocaleDateString(state.language === 'pt' ? 'pt-BR' : 'en-US', { day: '2-digit', month: 'short' })
            : '';

        const fallbackImg = 'https://placehold.co/500x300/12121a/8b5cf6?text=NexusNews';
        const optimizedImg = (article.imageUrl && !article.imageUrl.includes('placehold.co'))
            ? `https://wsrv.nl/?url=${encodeURIComponent(article.imageUrl)}&w=500&h=300&fit=cover&output=webp&q=80&default=${encodeURIComponent(fallbackImg)}`
            : fallbackImg;

        return `
            <article class="card-glow group rounded-2xl border border-white/[0.08] bg-zinc-900/50 backdrop-blur-xl overflow-hidden flex flex-col justify-between">
                <div>
                    <div class="h-44 w-full overflow-hidden bg-zinc-800/40 animate-pulse relative">
                        <img src="${optimizedImg}" 
                             alt="${article.title}" 
                             loading="lazy"
                             decoding="async"
                             referrerpolicy="no-referrer"
                             onload="this.parentElement.classList.remove('animate-pulse'); this.classList.remove('opacity-0');"
                             onerror="this.onerror=null; this.src='${fallbackImg}'; this.parentElement.classList.remove('animate-pulse'); this.classList.remove('opacity-0');"
                             class="w-full h-full object-cover group-hover:scale-105 transition-all duration-500 opacity-0">
                        <span class="absolute top-3 left-3 px-2.5 py-1 rounded-lg text-[10px] font-mono font-bold uppercase tracking-wider bg-zinc-950/85 text-violet-300 border border-violet-500/30 backdrop-blur-md">
                            ${article.category}
                        </span>
                    </div>

                    <div class="p-5">
                        <div class="flex items-center justify-between text-[11px] font-mono text-zinc-400 mb-2.5">
                            <span class="text-cyan-400 font-medium truncate max-w-[70%]">${article.sourceName}</span>
                            <span>${dateFormatted}</span>
                        </div>
                        <h3 class="text-base font-bold text-white group-hover:text-violet-300 transition line-clamp-2 mb-2 leading-snug">
                            ${article.title}
                        </h3>
                        <p class="text-xs text-zinc-400 line-clamp-3 leading-relaxed">
                            ${article.description}
                        </p>
                    </div>
                </div>

                <div class="px-5 pb-5 pt-2 border-t border-white/[0.04] mt-2">
                    <a href="${article.url}" target="_blank" rel="noopener noreferrer" 
                       class="inline-flex items-center text-xs font-semibold text-violet-400 group-hover:text-cyan-300 transition">
                        ${i18n[state.language].readArticle}
                    </a>
                </div>
            </article>
        `;
    }).join('');

    grid.insertAdjacentHTML('beforeend', html);
}

function updatePaginationButton() {
    const btn = document.getElementById('btn-load-more');
    const indicator = document.getElementById('timeframe-indicator');

    indicator.textContent = state.timeframe === 'week'
        ? i18n[state.language].timeframeWeek
        : i18n[state.language].timeframeMonth;

    if (state.hasMore) {
        btn.textContent = i18n[state.language].loadMorePage;
        btn.classList.remove('hidden');
    } else if (state.timeframe === 'week') {
        btn.textContent = i18n[state.language].expandToMonth;
        btn.classList.remove('hidden');
    } else {
        btn.classList.add('hidden');
    }
}

function loadMoreNews() {
    if (state.hasMore) {
        state.page += 1;
        fetchNews(false);
    } else if (state.timeframe === 'week') {
        state.timeframe = 'month';
        state.page = 1;
        fetchNews(true);
    }
}

function selectCategory(category) {
    state.category = category;
    state.timeframe = 'week';
    state.page = 1;

    document.querySelectorAll('.cat-pill').forEach(btn => {
        if (btn.dataset.cat === category) {
            btn.className = 'cat-pill px-4 py-2 rounded-xl text-xs font-semibold bg-gradient-to-r from-violet-600 to-indigo-600 text-white shadow-lg shadow-violet-600/25 transition whitespace-nowrap';
        } else {
            btn.className = 'cat-pill px-4 py-2 rounded-xl text-xs font-semibold bg-zinc-900/80 text-zinc-400 border border-white/[0.07] hover:text-white hover:border-violet-500/40 transition whitespace-nowrap';
        }
    });

    fetchNews(true);
}

function changeLanguage(lang) {
    state.language = lang;
    state.page = 1;

    // Atualiza classes dos botões PT/EN
    document.getElementById('lang-pt').className = lang === 'pt'
        ? 'px-3 py-1 rounded-lg bg-violet-600 text-white shadow-sm shadow-violet-600/40 transition'
        : 'px-3 py-1 rounded-lg text-zinc-400 hover:text-white transition';
    document.getElementById('lang-en').className = lang === 'en'
        ? 'px-3 py-1 rounded-lg bg-violet-600 text-white shadow-sm shadow-violet-600/40 transition'
        : 'px-3 py-1 rounded-lg text-zinc-400 hover:text-white transition';

    // Atualiza os placeholders dos inputs de assinatura
    document.getElementById('sub-name').placeholder = i18n[lang].placeholderName;
    document.getElementById('sub-email').placeholder = i18n[lang].placeholderEmail;

    // Atualiza os textos com data-i18n
    document.querySelectorAll('[data-i18n]').forEach(el => {
        const key = el.getAttribute('data-i18n');
        if (i18n[lang][key]) el.textContent = i18n[lang][key];
    });

    fetchNews(true);
}

async function handleSubscribe(e) {
    e.preventDefault();
    const nome = document.getElementById('sub-name').value.trim();
    const email = document.getElementById('sub-email').value.trim();
    const feedback = document.getElementById('sub-feedback');
    const submitBtn = document.getElementById('sub-submit-btn');

    submitBtn.disabled = true;
    feedback.classList.add('hidden');

    try {
        const response = await fetch(`${API_BASE_URL}/subscribers`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nome, email })
        });

        const data = await response.json();

        if (response.status === 202 || response.ok) {
            feedback.className = 'mt-4 text-xs font-medium rounded-xl p-3 max-w-xl mx-auto bg-emerald-500/10 border border-emerald-500/30 text-emerald-300';
            feedback.textContent = `${data.message || 'Inscrição recebida!'}: ${data.details || ''}`;
            document.getElementById('subscribe-form').reset();
        } else {
            feedback.className = 'mt-4 text-xs font-medium rounded-xl p-3 max-w-xl mx-auto bg-red-500/10 border border-red-500/30 text-red-300';
            feedback.textContent = data.message || 'Erro ao validar dados.';
        }
    } catch (err) {
        feedback.className = 'mt-4 text-xs font-medium rounded-xl p-3 max-w-xl mx-auto bg-red-500/10 border border-red-500/30 text-red-300';
        feedback.textContent = 'Falha de comunicação com o servidor.';
    } finally {
        feedback.classList.remove('hidden');
        submitBtn.disabled = false;
    }
}

function toggleLoginModal(show) {
    const modal = document.getElementById('login-modal');
    modal.classList.toggle('hidden', !show);
    modal.classList.toggle('flex', show);
}

function handleLogin(e) {
    e.preventDefault();
    state.jwtToken = 'mock-jwt-token';
    localStorage.setItem('nexus_jwt', state.jwtToken);
    toggleLoginModal(false);
    updateAuthUI();
    switchView('admin');
}

function updateAuthUI() {
    const adminBtn = document.getElementById('btn-admin-nav');
    if (state.jwtToken) adminBtn.classList.remove('hidden');
}

function switchView(view) {
    const portalView = document.getElementById('view-portal');
    const adminView = document.getElementById('view-admin');

    if (view === 'admin') {
        portalView.classList.add('hidden');
        adminView.classList.remove('hidden');
        fetchSubscribersAdmin();
    } else {
        adminView.classList.add('hidden');
        portalView.classList.remove('hidden');
    }
}

async function fetchSubscribersAdmin() {
    const tbody = document.getElementById('admin-subscribers-table');
    const countEl = document.getElementById('admin-sub-count');

    try {
        const response = await fetch(`${API_BASE_URL}/subscribers`);
        if (!response.ok) throw new Error('Falha ao buscar inscritos');
        const subscribers = await response.json();

        countEl.textContent = subscribers.length;

        if (subscribers.length === 0) {
            tbody.innerHTML = `<tr><td colspan="4" class="px-6 py-8 text-center text-zinc-500">Nenhum assinante cadastrado.</td></tr>`;
            return;
        }

        tbody.innerHTML = subscribers.map(sub => `
            <tr class="hover:bg-white/[0.03] transition">
                <td class="px-6 py-4 font-mono text-zinc-400">#${sub.id}</td>
                <td class="px-6 py-4 font-medium text-white">${sub.nome}</td>
                <td class="px-6 py-4 text-zinc-300">${sub.email}</td>
                <td class="px-6 py-4">
                    <span class="px-2.5 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-500/10 text-emerald-400 border border-emerald-500/20">
                        Ativo
                    </span>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        tbody.innerHTML = `<tr><td colspan="4" class="px-6 py-8 text-center text-red-400">Erro ao carregar assinantes.</td></tr>`;
    }
}

function triggerCustomFetch() {
    alert('Endpoint administrativo pronto para ser acoplado ao Spring Security + RabbitMQ!');
}