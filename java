// Dados fictícios dos produtos
const productsData = [
    { 
        id: 1, 
        name: "Laptop Gamer X500", 
        price: 6500.00, 
        img: "img/laptop.jpg",
        description: "Máximo desempenho para jogos e edição de vídeo."
    },
    { 
        id: 2, 
        name: "Smartphone Ultra Z Pro", 
        price: 3200.00, 
        img: "img/celular.jpg",
        description: "Câmera de 108MP e processador octa-core de última geração."
    },
    { 
        id: 3, 
        name: "Mouse Óptico ProSense", 
        price: 150.00, 
        img: "img/imagens.jpg",
        description: "Precisão de 16000 DPI e design ergonômico para longas sessões."
    },
    { 
        id: 4, 
        name: "Teclado Mecânico RGB Chroma", 
        price: 480.00, 
        img: "img/teclado.jpg",
        description: "Switches táteis de alta durabilidade e iluminação personalizável."
    },
    { 
        id: 5, 
        name: "Monitor Ultrawide 34'' 4K", 
        price: 2899.90, 
        img: "img/monitor.jpg", 
        description: "Experiência imersiva com tela curva e alta resolução."
    },
    { 
        id: 6, 
        name: "Fone de Ouvido Noise Canceling", 
        price: 750.50, 
        img: "img/fone.jpg", 
        description: "Cancelamento de ruído ativo para foco total no seu trabalho."
    },
    { 
        id: 7, 
        name: "Webcam Full HD Streamer", 
        price: 399.00, 
        img: "img/webcam.jpg", 
        description: "Vídeos em 1080p a 60fps para transmissões profissionais."
    },
    { 
        id: 8, 
        name: "Smartwatch Fitness T10", 
        price: 550.00, 
        img: "img/relogio.jpg", 
        description: "Monitoramento de saúde completo e bateria de longa duração."
    }
];

// --- Estrutura de Dados do Carrinho (Persistência com LocalStorage) ---
let cart = JSON.parse(localStorage.getItem('techshopCart')) || [];

// Requisito JavaScript: Criação de função para manipular o DOM e renderizar produtos
function renderProducts() {
    const productGrid = document.getElementById('products');
    productGrid.innerHTML = '<h2>Nossos Produtos em Destaque</h2>'; // Limpa o título para evitar duplicação

    productsData.forEach(product => {
        const productCard = document.createElement('div');
        productCard.className = 'product-card';
        
        productCard.innerHTML = `
            <img src="${product.img || 'https://via.placeholder.com/200'}" alt="${product.name}">
            <h4>${product.name}</h4>
            <p class="description">${product.description}</p>
            <p class="price">R$ ${product.price.toFixed(2).replace('.', ',')}</p>
            <button class="add-to-cart-btn" data-id="${product.id}">
                Adicionar ao Carrinho
            </button>
        `;
        productGrid.appendChild(productCard);
    });
}


function calculateCartTotal() {
    let subtotal = 0;
    // Requisito JavaScript: Uso de laço de repetição (forEach) para o cálculo
    cart.forEach(item => {
        subtotal += item.price * item.quantity;
    });

    const taxRate = 0.10; // Imposto fictício de 10%
    const totalWithTax = subtotal * (1 + taxRate);

    // Requisito JavaScript: Manipulação dinâmica de elementos HTML (Alteração de conteúdo)
    document.getElementById('cart-subtotal').textContent = `R$ ${subtotal.toFixed(2).replace('.', ',')}`;
    document.getElementById('cart-total').textContent = `R$ ${totalWithTax.toFixed(2).replace('.', ',')}`;
    document.getElementById('cart-count').textContent = cart.reduce((acc, item) => acc + item.quantity, 0);

    // Controla o botão de checkout
    const checkoutBtn = document.getElementById('go-to-checkout');
    const emptyMsg = document.getElementById('empty-cart-message');
    if (cart.length > 0) {
        checkoutBtn.style.display = 'block';
        emptyMsg.style.display = 'none';
    } else {
        checkoutBtn.style.display = 'none';
        emptyMsg.style.display = 'block';
    }

    // Salva no LocalStorage
    localStorage.setItem('techshopCart', JSON.stringify(cart));
}

// Requisito JavaScript: Criação de função para manipulação do DOM e renderizar o carrinho
function renderCart() {
    const cartList = document.getElementById('cart-items-list');
    cartList.innerHTML = ''; // Limpa a lista antes de renderizar

    if (cart.length === 0) {
        cartList.innerHTML = '<p style="text-align: center;">Carrinho vazio.</p>';
    } else {
        // Requisito JavaScript: Uso de laço de repetição para gerar a lista
        cart.forEach(item => {
            const itemDiv = document.createElement('div');
            itemDiv.className = 'cart-item';
            
            itemDiv.innerHTML = `
                <div class="cart-item-details">
                    <p><strong>${item.name}</strong></p>
                    <p>R$ ${item.price.toFixed(2).replace('.', ',')}</p>
                </div>
                <div>
                    <input type="number" min="1" value="${item.quantity}" data-id="${item.id}" class="item-quantity-input">
                </div>
                <button class="remove-btn" data-id="${item.id}">X</button>
            `;
            // Requisito JavaScript: Uso de evento onchange para atualização da quantidade
            const input = itemDiv.querySelector('.item-quantity-input');
            input.addEventListener('change', updateItemQuantity);

            // Requisito JavaScript: Uso de evento onclick e remoção dinâmica de elemento
            const removeBtn = itemDiv.querySelector('.remove-btn');
            removeBtn.addEventListener('click', removeItemFromCart);
            
            cartList.appendChild(itemDiv);
        });
    }

    calculateCartTotal();
}

// Requisito JavaScript: Uso de evento (onclick) e manipulação do DOM (adição)
function addItemToCart(event) {
    if (event.target.classList.contains('add-to-cart-btn')) {
        const productId = parseInt(event.target.dataset.id);
        const product = productsData.find(p => p.id === productId);

        // Requisito JavaScript: Uso de estruturas condicionais
        const existingItem = cart.find(item => item.id === productId);

        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            cart.push({ ...product, quantity: 1 });
        }
        
        renderCart();
        document.getElementById('cart-modal').classList.add('open');
    }
}

function updateItemQuantity(event) {
    const productId = parseInt(event.target.dataset.id);
    const newQuantity = parseInt(event.target.value);

    // Validação básica para evitar números negativos
    if (newQuantity < 1 || isNaN(newQuantity)) {
        alert('A quantidade deve ser no mínimo 1.');
        event.target.value = 1; // Volta para 1 ou último valor válido
        return;
    }
    
    const item = cart.find(item => item.id === productId);
    if (item) {
        item.quantity = newQuantity;
    }

    renderCart();
}

function removeItemFromCart(event) {
    const productId = parseInt(event.target.dataset.id);
    // Requisito JavaScript: Manipulação dinâmica de elementos HTML (remoção)
    cart = cart.filter(item => item.id !== productId);
    renderCart();
}

// --- Funções de Controle do Modal ---
function openCartModal() {
    document.getElementById('cart-modal').classList.add('open');
}

function closeCartModal() {
    document.getElementById('cart-modal').classList.remove('open');
}

// --- Inicialização e Event Listeners ---
document.addEventListener('DOMContentLoaded', () => {
    renderProducts();
    renderCart(); // Renderiza o carrinho ao carregar a página

    // Requisito JavaScript: Uso de eventos (onclick)
    document.getElementById('products').addEventListener('click', addItemToCart);
    document.getElementById('open-cart-btn').addEventListener('click', openCartModal);
    document.getElementById('close-cart-btn').addEventListener('click', closeCartModal);

    // Ouve os cliques no carrinho para remoção/alteração (delegado no renderCart)
});

// Implementação da Validação do Formulário (Se o usuário estiver em checkout.html)
if (window.location.pathname.includes('checkout.html')) {
    // Código JS para checkout.html
    const checkoutForm = document.getElementById('checkout-form');
    if (checkoutForm) {
        // Requisito JavaScript: Uso de evento onsubmit para validação
        checkoutForm.addEventListener('submit', function(event) {
            event.preventDefault(); // Evita o envio padrão

            if (validateForm()) {
                alert('✅ Pedido Finalizado com Sucesso! (Protótipo)');
                // Limpa o carrinho após a finalização
                localStorage.removeItem('techshopCart');
                window.location.href = 'index.html';
            }
        });
    }

    // Requisito JavaScript: Implementação de funcionalidade lógica real (Validação de formulários)
    function validateForm() {
        let isValid = true;
        const requiredFields = [
            { id: 'nome', label: 'Nome Completo' },
            { id: 'email', label: 'Email' },
            { id: 'cep', label: 'CEP' },
            { id: 'payment-method', label: 'Método de Pagamento' }
        ];

        // Limpa mensagens de erro antigas (Manipulação do DOM)
        document.querySelectorAll('.error-message').forEach(el => el.remove());

        // Requisito JavaScript: Uso de laço de repetição para percorrer campos
        requiredFields.forEach(field => {
            const input = document.getElementById(field.id);
            let value = input.value.trim();

            if (field.id === 'payment-method') {
                // Checa se algum radio button está selecionado
                if (!document.querySelector('input[name="payment"]:checked')) {
                     value = ""; // Força a falha na validação abaixo
                } else {
                    value = 'ok'; // Passa na validação
                }
            }

            // Requisito JavaScript: Uso de estruturas condicionais
            if (!value) {
                isValid = false;
                const errorDiv = document.createElement('div');
                errorDiv.className = 'error-message';
                errorDiv.style.color = 'red';
                errorDiv.textContent = `O campo ${field.label} é obrigatório.`;
                input.parentNode.insertBefore(errorDiv, input.nextSibling); // Insere após o campo
            }
        });

        return isValid;
    }
}
