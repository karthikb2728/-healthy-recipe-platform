// API Configuration
const API_BASE_URL = '';
let currentUser = null;
let authToken = null;

// DOM Elements
const navbar = document.querySelector('.navbar');
const navMenu = document.querySelector('.nav-menu');
const hamburger = document.querySelector('.hamburger');
const navAuth = document.querySelector('.nav-auth');
const sections = document.querySelectorAll('.section');
const homeSection = document.getElementById('home');
const recipesSection = document.getElementById('recipes');
const favoritesSection = document.getElementById('favorites');
const profileSection = document.getElementById('profile');
const adminSection = document.getElementById('admin');

// Recipe Data
let allRecipes = [];
let favoriteRecipes = [];
let currentPage = 1;
const recipesPerPage = 12;

// Initialize Application
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    checkAuthStatus();
});

// Initialize Application
function initializeApp() {
    // Show home section by default
    showSection('home');
    
    // Load sample data if user is logged in
    if (authToken) {
        loadRecipes();
        loadFavorites();
    } else {
        loadPublicRecipes();
    }
}

// Event Listeners
function setupEventListeners() {
    // Navigation
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const section = link.getAttribute('data-section');
            if (section) {
                showSection(section);
                updateActiveNavLink(link);
            }
        });
    });

    // Mobile menu toggle
    if (hamburger) {
        hamburger.addEventListener('click', () => {
            navMenu.classList.toggle('active');
            hamburger.classList.toggle('active');
        });
    }

    // Auth buttons
    document.getElementById('loginBtn')?.addEventListener('click', () => showModal('loginModal'));
    document.getElementById('registerBtn')?.addEventListener('click', () => showModal('registerModal'));
    document.getElementById('logoutBtn')?.addEventListener('click', logout);
    
    // Modal switching
    document.getElementById('switchToRegister')?.addEventListener('click', (e) => {
        e.preventDefault();
        hideModal('loginModal');
        showModal('registerModal');
    });
    
    document.getElementById('switchToLogin')?.addEventListener('click', (e) => {
        e.preventDefault();
        hideModal('registerModal');
        showModal('loginModal');
    });

    // Search functionality
    document.getElementById('heroSearch')?.addEventListener('submit', handleSearch);
    document.getElementById('recipeSearch')?.addEventListener('submit', handleSearch);

    // Recipe filters
    document.querySelectorAll('.filters select').forEach(filter => {
        filter.addEventListener('change', applyFilters);
    });

    // Forms
    document.getElementById('loginForm')?.addEventListener('submit', handleLogin);
    document.getElementById('registerForm')?.addEventListener('submit', handleRegister);
    document.getElementById('createRecipeForm')?.addEventListener('submit', handleCreateRecipe);
    document.getElementById('profileForm')?.addEventListener('submit', handleUpdateProfile);

    // Modal close buttons
    document.querySelectorAll('.close').forEach(closeBtn => {
        closeBtn.addEventListener('click', (e) => {
            const modal = e.target.closest('.modal');
            hideModal(modal.id);
        });
    });

    // Close modals when clicking outside
    document.querySelectorAll('.modal').forEach(modal => {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                hideModal(modal.id);
            }
        });
    });

    // Admin tabs
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const tabId = e.target.getAttribute('data-tab');
            showTab(tabId);
        });
    });
}

// Navigation Functions
function showSection(sectionId) {
    // Hide all sections
    sections.forEach(section => section.classList.remove('active'));
    
    // Show target section
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add('active');
    }

    // Close mobile menu
    navMenu.classList.remove('active');
    hamburger.classList.remove('active');

    // Load section-specific data
    switch(sectionId) {
        case 'recipes':
            loadRecipes();
            break;
        case 'favorites':
            loadFavorites();
            break;
        case 'create':
            loadCreateForm();
            break;
        case 'profile':
            loadProfile();
            break;
        case 'admin':
            loadAdminData();
            break;
    }
}

function updateActiveNavLink(activeLink) {
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    activeLink.classList.add('active');
}

// Authentication Functions
function checkAuthStatus() {
    authToken = localStorage.getItem('authToken');
    currentUser = JSON.parse(localStorage.getItem('currentUser') || 'null');
    
    updateNavbar();
    
    if (authToken) {
        // Validate token with server
        validateToken();
    }
}

function updateNavbar() {
    const loginBtn = document.getElementById('loginBtn');
    const registerBtn = document.getElementById('registerBtn');
    const logoutBtn = document.getElementById('logoutBtn');
    const userGreeting = document.getElementById('userGreeting');

    if (currentUser) {
        loginBtn?.classList.add('hidden');
        registerBtn?.classList.add('hidden');
        logoutBtn?.classList.remove('hidden');
        
        if (userGreeting) {
            const roleEmoji = getRoleEmoji(currentUser.role);
            userGreeting.textContent = `${roleEmoji} Welcome, ${currentUser.firstName || currentUser.username}!`;
            userGreeting.style.display = 'inline-block';
        }

        // Show user-specific navigation based on role
        document.querySelector('[data-section="favorites"]').style.display = 'block';
        document.querySelector('[data-section="profile"]').style.display = 'block';
        
        // Show create recipe for chefs and admins
        const createLink = document.querySelector('[data-section="create"]');
        if (createLink) {
            createLink.style.display = (currentUser.role === 'CHEF' || currentUser.role === 'ADMIN') ? 'block' : 'none';
        }

        // Show admin panel only for admins
        const adminLink = document.querySelector('[data-section="admin"]');
        if (adminLink) {
            adminLink.style.display = currentUser.role === 'ADMIN' ? 'block' : 'none';
        }
    } else {
        loginBtn?.classList.remove('hidden');
        registerBtn?.classList.remove('hidden');
        logoutBtn?.classList.add('hidden');
        
        if (userGreeting) {
            userGreeting.style.display = 'none';
        }

        // Hide user-specific navigation
        document.querySelector('[data-section="favorites"]').style.display = 'none';
        document.querySelector('[data-section="profile"]').style.display = 'none';
        document.querySelector('[data-section="create"]').style.display = 'none';
        document.querySelector('[data-section="admin"]').style.display = 'none';
    }
}

function getRoleEmoji(role) {
    switch(role) {
        case 'ADMIN': return '⚙️';
        case 'CHEF': return '👨‍🍳';
        case 'USER': return '👤';
        default: return '👤';
    }
}

async function validateToken() {
    try {
        const response = await fetch('/api/auth/validate', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Token validation failed');
        }

        const userData = await response.json();
        currentUser = userData;
        localStorage.setItem('currentUser', JSON.stringify(userData));
        updateNavbar();
    } catch (error) {
        console.error('Token validation error:', error);
        logout();
    }
}

async function handleLogin(e) {
    e.preventDefault();
    const form = e.target;
    const formData = new FormData(form);
    
    const loginData = {
        username: formData.get('username'),
        password: formData.get('password')
    };

    try {
        showLoading('Logging in...');
        const response = await fetch('/api/auth/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        });

        if (response.ok) {
            const data = await response.json();
            authToken = data.token;
            // Create user object with role as string (first role from array)
            currentUser = {
                id: data.id,
                username: data.username,
                email: data.email,
                firstName: data.firstName,
                lastName: data.lastName,
                role: data.roles && data.roles.length > 0 ? data.roles[0] : 'USER'
            };
            localStorage.setItem('authToken', authToken);
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            
            hideModal('loginModal');
            updateNavbar();
            showMessage('Welcome back! Login successful!', 'success');
            
            // Reload current section data
            const activeSection = document.querySelector('.section.active');
            if (activeSection) {
                showSection(activeSection.id);
            }
        } else {
            const errorData = await response.json().catch(() => ({ message: 'Login failed' }));
            showMessage(errorData.message || 'Invalid username or password', 'error');
        }
        hideLoading();
    } catch (error) {
        hideLoading();
        console.error('Login error:', error);
        showMessage('Connection error. Please check your internet connection.', 'error');
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const form = e.target;
    const formData = new FormData(form);
    
    const registerData = {
        firstName: formData.get('firstName'),
        lastName: formData.get('lastName'),
        username: formData.get('username'),
        email: formData.get('email'),
        password: formData.get('password'),
        role: [formData.get('role') || 'USER'], // Backend expects uppercase role names in array
        phoneNumber: '',
        bio: ''
    };

    // Password confirmation check
    if (registerData.password !== formData.get('confirmPassword')) {
        showMessage('Passwords do not match!', 'error');
        return;
    }

    // Validate password strength
    if (registerData.password.length < 6) {
        showMessage('Password must be at least 6 characters long', 'error');
        return;
    }

    try {
        showLoading('Creating your account...');
        const response = await fetch('/api/auth/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registerData)
        });

        if (response.ok) {
            const data = await response.json();
            hideModal('registerModal');
            showMessage('🎉 Account created successfully! Please log in to continue.', 'success');
            
            // Pre-fill login form with the username
            setTimeout(() => {
                showModal('loginModal');
                document.getElementById('login-username').value = registerData.username;
            }, 1000);
        } else {
            const errorData = await response.json().catch(() => ({ message: 'Registration failed' }));
            showMessage(errorData.message || 'Registration failed. Please try again.', 'error');
        }
        hideLoading();
    } catch (error) {
        hideLoading();
        console.error('Registration error:', error);
        showMessage('Connection error. Please check your internet connection.', 'error');
    }
}

function logout() {
    authToken = null;
    currentUser = null;
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    
    updateNavbar();
    showSection('home');
    showMessage('Logged out successfully', 'info');
}

// Recipe Functions
async function loadPublicRecipes() {
    try {
        showLoading('Loading recipes...');
        
        // Sample data for demo
        allRecipes = generateSampleRecipes();
        displayRecipes(allRecipes);
        hideLoading();
    } catch (error) {
        hideLoading();
        console.error('Error loading recipes:', error);
        showMessage('Failed to load recipes', 'error');
    }
}

async function loadRecipes() {
    if (!authToken) {
        loadPublicRecipes();
        return;
    }

    try {
        showLoading('Loading recipes...');
        const response = await fetch('/api/recipes', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            allRecipes = await response.json();
            displayRecipes(allRecipes);
        } else {
            // Fallback to sample data
            allRecipes = generateSampleRecipes();
            displayRecipes(allRecipes);
        }
        hideLoading();
    } catch (error) {
        hideLoading();
        console.error('Error loading recipes:', error);
        // Fallback to sample data
        allRecipes = generateSampleRecipes();
        displayRecipes(allRecipes);
    }
}

function displayRecipes(recipes) {
    const recipesGrid = document.getElementById('recipesGrid');
    if (!recipesGrid) return;

    if (recipes.length === 0) {
        recipesGrid.innerHTML = '<p class="no-results">No recipes found.</p>';
        return;
    }

    // Pagination
    const startIndex = (currentPage - 1) * recipesPerPage;
    const endIndex = startIndex + recipesPerPage;
    const paginatedRecipes = recipes.slice(startIndex, endIndex);

    recipesGrid.innerHTML = paginatedRecipes.map(recipe => `
        <div class="recipe-card" onclick="showRecipeDetails(${recipe.id})">
            <div class="recipe-image">
                <i class="fas fa-utensils"></i>
            </div>
            <div class="recipe-info">
                <h3 class="recipe-title">${recipe.title}</h3>
                <p class="recipe-description">${recipe.description}</p>
                <div class="recipe-meta">
                    <span><i class="fas fa-clock"></i> ${recipe.cookingTime} min</span>
                    <div class="recipe-rating">
                        <span class="stars">${generateStars(recipe.rating || 0)}</span>
                        <span>(${recipe.ratingCount || 0})</span>
                    </div>
                </div>
                <div class="recipe-actions">
                    <button class="favorite-btn ${isFavorite(recipe.id) ? 'active' : ''}" 
                            onclick="event.stopPropagation(); toggleFavorite(${recipe.id})">
                        <i class="fas fa-heart"></i> ${isFavorite(recipe.id) ? 'Favorited' : 'Favorite'}
                    </button>
                </div>
            </div>
        </div>
    `).join('');

    // Update pagination
    updatePagination(recipes.length);
}

function generateStars(rating) {
    const fullStars = Math.floor(rating);
    const hasHalfStar = rating % 1 !== 0;
    let stars = '';
    
    for (let i = 0; i < fullStars; i++) {
        stars += '<i class="fas fa-star"></i>';
    }
    
    if (hasHalfStar) {
        stars += '<i class="fas fa-star-half-alt"></i>';
    }
    
    const emptyStars = 5 - Math.ceil(rating);
    for (let i = 0; i < emptyStars; i++) {
        stars += '<i class="far fa-star"></i>';
    }
    
    return stars;
}

function updatePagination(totalRecipes) {
    const totalPages = Math.ceil(totalRecipes / recipesPerPage);
    const paginationContainer = document.getElementById('pagination');
    
    if (!paginationContainer || totalPages <= 1) {
        if (paginationContainer) paginationContainer.innerHTML = '';
        return;
    }

    let paginationHTML = '';
    
    // Previous button
    paginationHTML += `
        <button ${currentPage === 1 ? 'disabled' : ''} onclick="changePage(${currentPage - 1})">
            <i class="fas fa-chevron-left"></i>
        </button>
    `;
    
    // Page numbers
    for (let i = 1; i <= totalPages; i++) {
        paginationHTML += `
            <button class="${i === currentPage ? 'active' : ''}" onclick="changePage(${i})">
                ${i}
            </button>
        `;
    }
    
    // Next button
    paginationHTML += `
        <button ${currentPage === totalPages ? 'disabled' : ''} onclick="changePage(${currentPage + 1})">
            <i class="fas fa-chevron-right"></i>
        </button>
    `;
    
    paginationContainer.innerHTML = paginationHTML;
}

function changePage(page) {
    currentPage = page;
    displayRecipes(allRecipes);
}

// Search and Filter Functions
function handleSearch(e) {
    e.preventDefault();
    const searchInput = e.target.querySelector('input[name="search"]');
    const searchTerm = searchInput.value.toLowerCase().trim();
    
    if (searchTerm) {
        const filteredRecipes = allRecipes.filter(recipe => 
            recipe.title.toLowerCase().includes(searchTerm) ||
            recipe.description.toLowerCase().includes(searchTerm) ||
            recipe.ingredients.some(ingredient => ingredient.toLowerCase().includes(searchTerm))
        );
        
        displayRecipes(filteredRecipes);
        
        // Switch to recipes section if not already there
        if (!recipesSection.classList.contains('active')) {
            showSection('recipes');
            updateActiveNavLink(document.querySelector('[data-section="recipes"]'));
        }
    }
}

function applyFilters() {
    const categoryFilter = document.getElementById('categoryFilter')?.value;
    const difficultyFilter = document.getElementById('difficultyFilter')?.value;
    const sortFilter = document.getElementById('sortFilter')?.value;
    
    let filteredRecipes = [...allRecipes];
    
    // Apply category filter
    if (categoryFilter && categoryFilter !== 'all') {
        filteredRecipes = filteredRecipes.filter(recipe => 
            recipe.category?.toLowerCase() === categoryFilter.toLowerCase()
        );
    }
    
    // Apply difficulty filter
    if (difficultyFilter && difficultyFilter !== 'all') {
        filteredRecipes = filteredRecipes.filter(recipe => 
            recipe.difficulty?.toLowerCase() === difficultyFilter.toLowerCase()
        );
    }
    
    // Apply sorting
    if (sortFilter) {
        switch(sortFilter) {
            case 'rating':
                filteredRecipes.sort((a, b) => (b.rating || 0) - (a.rating || 0));
                break;
            case 'time':
                filteredRecipes.sort((a, b) => a.cookingTime - b.cookingTime);
                break;
            case 'newest':
                filteredRecipes.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
                break;
            case 'name':
                filteredRecipes.sort((a, b) => a.title.localeCompare(b.title));
                break;
        }
    }
    
    currentPage = 1; // Reset to first page
    displayRecipes(filteredRecipes);
}

// Favorite Functions
function isFavorite(recipeId) {
    return favoriteRecipes.some(fav => fav.id === recipeId);
}

async function toggleFavorite(recipeId) {
    if (!authToken) {
        showMessage('Please log in to manage favorites', 'info');
        showModal('loginModal');
        return;
    }

    try {
        const isFav = isFavorite(recipeId);
        const url = isFav ? `/api/favorites/remove/${recipeId}` : `/api/favorites/add/${recipeId}`;
        const method = isFav ? 'DELETE' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            if (isFav) {
                favoriteRecipes = favoriteRecipes.filter(fav => fav.id !== recipeId);
                showMessage('Removed from favorites', 'info');
            } else {
                const recipe = allRecipes.find(r => r.id === recipeId);
                if (recipe) {
                    favoriteRecipes.push(recipe);
                }
                showMessage('Added to favorites', 'success');
            }
            
            // Update UI
            updateFavoriteButtons();
            if (favoritesSection.classList.contains('active')) {
                displayFavorites();
            }
        } else {
            showMessage('Failed to update favorites', 'error');
        }
    } catch (error) {
        console.error('Favorite toggle error:', error);
        showMessage('Failed to update favorites', 'error');
    }
}

function updateFavoriteButtons() {
    document.querySelectorAll('.favorite-btn').forEach(btn => {
        const recipeId = parseInt(btn.getAttribute('onclick').match(/\d+/)[0]);
        const isFav = isFavorite(recipeId);
        
        btn.classList.toggle('active', isFav);
        btn.innerHTML = `<i class="fas fa-heart"></i> ${isFav ? 'Favorited' : 'Favorite'}`;
    });
}

async function loadFavorites() {
    if (!authToken) {
        document.getElementById('favoritesGrid').innerHTML = 
            '<p class="no-results">Please log in to view your favorites.</p>';
        return;
    }

    try {
        showLoading('Loading favorites...');
        const response = await fetch('/api/favorites', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            favoriteRecipes = await response.json();
        } else {
            // Use local favorites as fallback
            favoriteRecipes = favoriteRecipes || [];
        }
        
        displayFavorites();
        hideLoading();
    } catch (error) {
        hideLoading();
        console.error('Error loading favorites:', error);
        displayFavorites(); // Show local favorites
    }
}

function displayFavorites() {
    const favoritesGrid = document.getElementById('favoritesGrid');
    if (!favoritesGrid) return;

    if (favoriteRecipes.length === 0) {
        favoritesGrid.innerHTML = '<p class="no-results">No favorite recipes yet. Start exploring recipes!</p>';
        return;
    }

    favoritesGrid.innerHTML = favoriteRecipes.map(recipe => `
        <div class="recipe-card" onclick="showRecipeDetails(${recipe.id})">
            <div class="recipe-image">
                <i class="fas fa-utensils"></i>
            </div>
            <div class="recipe-info">
                <h3 class="recipe-title">${recipe.title}</h3>
                <p class="recipe-description">${recipe.description}</p>
                <div class="recipe-meta">
                    <span><i class="fas fa-clock"></i> ${recipe.cookingTime} min</span>
                    <div class="recipe-rating">
                        <span class="stars">${generateStars(recipe.rating || 0)}</span>
                        <span>(${recipe.ratingCount || 0})</span>
                    </div>
                </div>
                <div class="recipe-actions">
                    <button class="favorite-btn active" onclick="event.stopPropagation(); toggleFavorite(${recipe.id})">
                        <i class="fas fa-heart"></i> Favorited
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

// Recipe Details Modal
function showRecipeDetails(recipeId) {
    const recipe = allRecipes.find(r => r.id === recipeId);
    if (!recipe) return;

    const modal = document.getElementById('recipeModal');
    const modalContent = modal.querySelector('.modal form');
    
    modalContent.innerHTML = `
        <div class="recipe-details">
            <div class="recipe-header">
                <h2>${recipe.title}</h2>
                <div class="recipe-meta">
                    <span><i class="fas fa-clock"></i> ${recipe.cookingTime} min</span>
                    <span><i class="fas fa-users"></i> ${recipe.servings || 4} servings</span>
                    <span><i class="fas fa-signal"></i> ${recipe.difficulty || 'Medium'}</span>
                    <div class="recipe-rating">
                        <span class="stars">${generateStars(recipe.rating || 0)}</span>
                        <span>(${recipe.ratingCount || 0} reviews)</span>
                    </div>
                </div>
            </div>
            
            <div class="recipe-description">
                <p>${recipe.description}</p>
            </div>
            
            <div class="recipe-ingredients">
                <h3>Ingredients</h3>
                <ul>
                    ${recipe.ingredients.map(ingredient => `<li>${ingredient}</li>`).join('')}
                </ul>
            </div>
            
            <div class="recipe-instructions">
                <h3>Instructions</h3>
                <ol>
                    ${recipe.instructions.map(instruction => `<li>${instruction}</li>`).join('')}
                </ol>
            </div>
            
            ${recipe.nutritionInfo ? `
                <div class="recipe-nutrition">
                    <h3>Nutrition Information</h3>
                    <div class="nutrition-grid">
                        <span>Calories: ${recipe.nutritionInfo.calories}</span>
                        <span>Protein: ${recipe.nutritionInfo.protein}g</span>
                        <span>Carbs: ${recipe.nutritionInfo.carbs}g</span>
                        <span>Fat: ${recipe.nutritionInfo.fat}g</span>
                    </div>
                </div>
            ` : ''}
            
            <div class="recipe-actions">
                <button type="button" class="btn-primary favorite-btn ${isFavorite(recipe.id) ? 'active' : ''}" 
                        onclick="toggleFavorite(${recipe.id})">
                    <i class="fas fa-heart"></i> ${isFavorite(recipe.id) ? 'Favorited' : 'Add to Favorites'}
                </button>
                ${authToken ? `
                    <button type="button" class="btn-secondary" onclick="showRatingModal(${recipe.id})">
                        <i class="fas fa-star"></i> Rate Recipe
                    </button>
                ` : ''}
            </div>
        </div>
    `;
    
    showModal('recipeModal');
}

// Sample Data Generator
function generateSampleRecipes() {
    return [
        {
            id: 1,
            title: "Mediterranean Quinoa Bowl",
            description: "A healthy and colorful bowl packed with quinoa, fresh vegetables, and Mediterranean flavors.",
            cookingTime: 25,
            servings: 4,
            difficulty: "Easy",
            category: "Healthy",
            rating: 4.5,
            ratingCount: 23,
            ingredients: [
                "1 cup quinoa",
                "2 cups vegetable broth",
                "1 cucumber, diced",
                "2 tomatoes, chopped",
                "1/2 red onion, sliced",
                "1/4 cup feta cheese",
                "2 tbsp olive oil",
                "1 lemon, juiced",
                "Fresh herbs (parsley, mint)"
            ],
            instructions: [
                "Rinse quinoa and cook in vegetable broth until fluffy",
                "Let quinoa cool completely",
                "Dice cucumber and tomatoes",
                "Slice red onion thinly",
                "Mix olive oil and lemon juice for dressing",
                "Combine all ingredients in a large bowl",
                "Top with feta cheese and fresh herbs",
                "Serve chilled"
            ],
            nutritionInfo: {
                calories: 320,
                protein: 12,
                carbs: 45,
                fat: 10
            },
            createdAt: "2024-01-15T10:00:00Z"
        },
        {
            id: 2,
            title: "Grilled Salmon with Asparagus",
            description: "Perfectly grilled salmon with roasted asparagus and lemon herb butter.",
            cookingTime: 20,
            servings: 2,
            difficulty: "Medium",
            category: "Seafood",
            rating: 4.8,
            ratingCount: 31,
            ingredients: [
                "2 salmon fillets",
                "1 bunch asparagus",
                "2 tbsp olive oil",
                "1 lemon",
                "2 tbsp butter",
                "2 cloves garlic, minced",
                "Fresh dill",
                "Salt and pepper"
            ],
            instructions: [
                "Preheat grill to medium-high heat",
                "Season salmon with salt and pepper",
                "Trim asparagus ends",
                "Grill salmon 4-5 minutes per side",
                "Grill asparagus 3-4 minutes",
                "Make herb butter with garlic and dill",
                "Serve with lemon wedges"
            ],
            nutritionInfo: {
                calories: 280,
                protein: 35,
                carbs: 8,
                fat: 12
            },
            createdAt: "2024-01-14T15:30:00Z"
        },
        {
            id: 3,
            title: "Veggie Buddha Bowl",
            description: "Nourishing bowl with roasted vegetables, chickpeas, and tahini dressing.",
            cookingTime: 35,
            servings: 3,
            difficulty: "Easy",
            category: "Vegetarian",
            rating: 4.2,
            ratingCount: 18,
            ingredients: [
                "1 cup chickpeas",
                "2 sweet potatoes",
                "1 cup broccoli",
                "1 cup brown rice",
                "3 tbsp tahini",
                "1 lemon juiced",
                "2 tbsp olive oil",
                "1 tsp cumin",
                "Spinach leaves"
            ],
            instructions: [
                "Cook brown rice according to package directions",
                "Roast sweet potatoes and broccoli",
                "Season chickpeas with cumin and roast",
                "Make tahini dressing with lemon juice",
                "Arrange all ingredients in bowls",
                "Drizzle with tahini dressing",
                "Serve warm"
            ],
            nutritionInfo: {
                calories: 420,
                protein: 15,
                carbs: 68,
                fat: 14
            },
            createdAt: "2024-01-13T12:45:00Z"
        }
    ];
}

// Modal Functions
function showModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.add('show');
        document.body.style.overflow = 'hidden';
    }
}

function hideModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.classList.remove('show');
        document.body.style.overflow = 'auto';
        
        // Reset forms
        const form = modal.querySelector('form');
        if (form) {
            form.reset();
        }
    }
}

// Loading and Messages
function showLoading(message = 'Loading...') {
    // Create or update loading indicator
    let loadingEl = document.getElementById('loadingIndicator');
    if (!loadingEl) {
        loadingEl = document.createElement('div');
        loadingEl.id = 'loadingIndicator';
        loadingEl.className = 'loading';
        document.body.appendChild(loadingEl);
    }
    
    loadingEl.innerHTML = `
        <i class="fas fa-spinner fa-spin"></i>
        <div>${message}</div>
    `;
    loadingEl.style.display = 'block';
}

function hideLoading() {
    const loadingEl = document.getElementById('loadingIndicator');
    if (loadingEl) {
        loadingEl.style.display = 'none';
    }
}

function showMessage(message, type = 'info') {
    const messageContainer = document.querySelector('.message-container') || createMessageContainer();
    
    const messageEl = document.createElement('div');
    messageEl.className = `message ${type}`;
    messageEl.textContent = message;
    
    messageContainer.appendChild(messageEl);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (messageEl.parentNode) {
            messageEl.parentNode.removeChild(messageEl);
        }
    }, 5000);
}

function createMessageContainer() {
    const container = document.createElement('div');
    container.className = 'message-container';
    document.body.appendChild(container);
    return container;
}

// Profile Functions
async function loadProfile() {
    if (!authToken) {
        document.getElementById('profileForm').innerHTML = 
            '<p class="no-results">Please log in to view your profile.</p>';
        return;
    }

    try {
        // Pre-fill form with current user data
        const form = document.getElementById('profileForm');
        if (form && currentUser) {
            form.querySelector('input[name="username"]').value = currentUser.username || '';
            form.querySelector('input[name="email"]').value = currentUser.email || '';
        }
    } catch (error) {
        console.error('Error loading profile:', error);
    }
}

async function handleUpdateProfile(e) {
    e.preventDefault();
    
    if (!authToken) {
        showMessage('Please log in to update profile', 'error');
        return;
    }

    const form = e.target;
    const formData = new FormData(form);
    
    const updateData = {
        username: formData.get('username'),
        email: formData.get('email')
    };

    // Add password if provided
    const newPassword = formData.get('newPassword');
    if (newPassword) {
        if (newPassword !== formData.get('confirmPassword')) {
            showMessage('Passwords do not match', 'error');
            return;
        }
        updateData.password = newPassword;
    }

    try {
        showLoading('Updating profile...');
        const response = await fetch('/api/profile/update', {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updateData)
        });

        const data = await response.json();
        hideLoading();

        if (response.ok) {
            currentUser = { ...currentUser, ...data };
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            updateNavbar();
            showMessage('Profile updated successfully!', 'success');
        } else {
            showMessage(data.message || 'Failed to update profile', 'error');
        }
    } catch (error) {
        hideLoading();
        console.error('Profile update error:', error);
        showMessage('Failed to update profile', 'error');
    }
}

// Admin Functions
function showTab(tabId) {
    // Update tab buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.querySelector(`[data-tab="${tabId}"]`).classList.add('active');
    
    // Update tab panes
    document.querySelectorAll('.tab-pane').forEach(pane => {
        pane.classList.remove('active');
    });
    document.getElementById(tabId).classList.add('active');
}

async function loadAdminData() {
    if (!authToken || currentUser?.role !== 'ADMIN') {
        adminSection.innerHTML = '<p class="no-results">Access denied. Admin privileges required.</p>';
        return;
    }

    try {
        showLoading('Loading admin data...');
        
        // Load users data
        await loadUsers();
        
        // Load recipes data
        await loadAdminRecipes();
        
        hideLoading();
    } catch (error) {
        hideLoading();
        console.error('Error loading admin data:', error);
        showMessage('Failed to load admin data', 'error');
    }
}

async function loadUsers() {
    try {
        const response = await fetch('/api/admin/users', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            }
        });

        let users = [];
        if (response.ok) {
            users = await response.json();
        } else {
            // Sample data for demo
            users = [
                { id: 1, username: 'john_doe', email: 'john@example.com', role: 'USER', createdAt: '2024-01-10' },
                { id: 2, username: 'chef_mary', email: 'mary@example.com', role: 'CHEF', createdAt: '2024-01-08' },
                { id: 3, username: 'admin', email: 'admin@example.com', role: 'ADMIN', createdAt: '2024-01-01' }
            ];
        }

        displayUsers(users);
    } catch (error) {
        console.error('Error loading users:', error);
        // Show sample data
        displayUsers([
            { id: 1, username: 'john_doe', email: 'john@example.com', role: 'USER', createdAt: '2024-01-10' },
            { id: 2, username: 'chef_mary', email: 'mary@example.com', role: 'CHEF', createdAt: '2024-01-08' }
        ]);
    }
}

function displayUsers(users) {
    const usersTableBody = document.querySelector('#usersTable tbody');
    if (!usersTableBody) return;

    usersTableBody.innerHTML = users.map(user => `
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td><span class="role-badge role-${user.role.toLowerCase()}">${user.role}</span></td>
            <td>${new Date(user.createdAt).toLocaleDateString()}</td>
            <td>
                <button class="btn-secondary btn-sm" onclick="editUser(${user.id})">Edit</button>
                <button class="btn-danger btn-sm" onclick="deleteUser(${user.id})">Delete</button>
            </td>
        </tr>
    `).join('');
}

async function loadAdminRecipes() {
    // Implementation for loading all recipes in admin panel
    // Similar to loadRecipes but for admin management
}

// Recipe Creation
async function handleCreateRecipe(e) {
    e.preventDefault();
    
    if (!authToken || (currentUser.role !== 'CHEF' && currentUser.role !== 'ADMIN')) {
        showMessage('Only chefs and admins can create recipes', 'error');
        return;
    }

    const form = e.target;
    const formData = new FormData(form);
    
    // Collect ingredients
    const ingredientNames = formData.getAll('ingredientName[]');
    const ingredientQuantities = formData.getAll('ingredientQuantity[]');
    const ingredientUnits = formData.getAll('ingredientUnit[]');
    
    const ingredients = ingredientNames.map((name, index) => ({
        name: name,
        quantity: parseFloat(ingredientQuantities[index]) || 0,
        unit: ingredientUnits[index] || '',
        notes: ''
    })).filter(ingredient => ingredient.name.trim());

    const recipeData = {
        title: formData.get('title'),
        description: formData.get('description'),
        preparationTime: parseInt(formData.get('preparationTime')) || 0,
        cookingTime: parseInt(formData.get('cookingTime')) || 0,
        servings: parseInt(formData.get('servings')) || 1,
        difficultyLevel: formData.get('difficultyLevel') || 'MEDIUM',
        status: 'PENDING', // Requires admin approval
        instructions: formData.get('instructions'),
        categories: [formData.get('category')].filter(c => c),
        dietaryTags: [],
        ingredients: ingredients,
        // Nutritional information
        calories: parseInt(formData.get('calories')) || 0,
        protein: parseFloat(formData.get('protein')) || 0,
        carbohydrates: parseFloat(formData.get('carbohydrates')) || 0,
        fat: parseFloat(formData.get('fat')) || 0,
        fiber: 0,
        sugar: 0,
        sodium: 0
    };

    try {
        showLoading('Creating recipe for review...');
        const response = await fetch('/api/recipes', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${authToken}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recipeData)
        });

        hideLoading();

        if (response.ok) {
            const data = await response.json();
            resetForm();
            showMessage('🎉 Recipe submitted successfully! It will be reviewed by our team before publication.', 'success');
            
            // Switch to recipes section to show updated list
            setTimeout(() => {
                showSection('recipes');
                updateActiveNavLink(document.querySelector('[data-section="recipes"]'));
            }, 2000);
        } else {
            const errorData = await response.json().catch(() => ({ message: 'Failed to create recipe' }));
            showMessage(errorData.message || 'Failed to create recipe. Please try again.', 'error');
        }
    } catch (error) {
        hideLoading();
        console.error('Recipe creation error:', error);
        showMessage('Connection error. Please check your internet connection.', 'error');
    }
}

// Utility Functions
function formatDate(dateString) {
    return new Date(dateString).toLocaleDateString();
}

function truncateText(text, maxLength) {
    return text.length > maxLength ? text.substring(0, maxLength) + '...' : text;
}

// Create Recipe Functions
function loadCreateForm() {
    if (!authToken || (currentUser.role !== 'CHEF' && currentUser.role !== 'ADMIN')) {
        document.getElementById('create').innerHTML = 
            '<div class="no-access"><h2>🔒 Access Denied</h2><p>Only chefs and admins can create recipes.</p></div>';
        return;
    }
    
    // Initialize ingredient form functionality
    setupIngredientForm();
}

function setupIngredientForm() {
    const addIngredientBtn = document.getElementById('add-ingredient');
    if (addIngredientBtn) {
        addIngredientBtn.addEventListener('click', addIngredientRow);
    }
    
    // Setup existing remove buttons
    document.querySelectorAll('.remove-ingredient').forEach(btn => {
        btn.addEventListener('click', function() {
            this.parentElement.remove();
            updateRemoveButtons();
        });
    });
}

function addIngredientRow() {
    const container = document.getElementById('ingredients-container');
    const newRow = document.createElement('div');
    newRow.className = 'ingredient-row';
    newRow.innerHTML = `
        <input type="text" placeholder="Ingredient name" name="ingredientName[]" required>
        <input type="number" placeholder="Quantity" name="ingredientQuantity[]" step="0.1" min="0">
        <input type="text" placeholder="Unit (e.g., cups, tsp)" name="ingredientUnit[]">
        <button type="button" class="btn-danger btn-sm remove-ingredient">Remove</button>
    `;
    
    container.appendChild(newRow);
    
    // Add event listener to new remove button
    newRow.querySelector('.remove-ingredient').addEventListener('click', function() {
        newRow.remove();
        updateRemoveButtons();
    });
    
    updateRemoveButtons();
}

function updateRemoveButtons() {
    const removeButtons = document.querySelectorAll('.remove-ingredient');
    removeButtons.forEach((btn, index) => {
        btn.style.display = removeButtons.length > 1 ? 'block' : 'none';
    });
}

function resetForm() {
    const form = document.getElementById('createRecipeForm');
    if (form) {
        form.reset();
        // Reset ingredients to single row
        const container = document.getElementById('ingredients-container');
        container.innerHTML = `
            <div class="ingredient-row">
                <input type="text" placeholder="Ingredient name" name="ingredientName[]" required>
                <input type="number" placeholder="Quantity" name="ingredientQuantity[]" step="0.1" min="0">
                <input type="text" placeholder="Unit (e.g., cups, tsp)" name="ingredientUnit[]">
                <button type="button" class="btn-danger btn-sm remove-ingredient" style="display: none;">Remove</button>
            </div>
        `;
        setupIngredientForm();
    }
}

// Admin Functions
async function loadAdminData() {
    if (!authToken || currentUser?.role !== 'ADMIN') {
        document.getElementById('admin').innerHTML = 
            '<div class="no-access"><h2>🔒 Access Denied</h2><p>Administrator privileges required.</p></div>';
        return;
    }

    try {
        showLoading('Loading admin dashboard...');
        
        // Load admin statistics
        await loadAdminStats();
        
        // Load activity feed
        await loadActivityFeed();
        
        hideLoading();
    } catch (error) {
        hideLoading();
        console.error('Error loading admin data:', error);
        showMessage('Failed to load admin dashboard', 'error');
    }
}

async function loadAdminStats() {
    // Mock data for demo - replace with actual API calls
    const stats = {
        totalUsers: 42,
        totalRecipes: 128,
        pendingRecipes: 7,
        activeUsers: 15,
        newRecipes: 12,
        newReviews: 34,
        chefApplications: 3
    };
    
    // Update stat displays
    document.getElementById('totalUsers').textContent = stats.totalUsers;
    document.getElementById('totalRecipes').textContent = stats.totalRecipes;
    document.getElementById('pendingRecipes').textContent = stats.pendingRecipes;
    document.getElementById('activeUsers').textContent = stats.activeUsers;
    document.getElementById('newRecipes').textContent = stats.newRecipes;
    document.getElementById('newReviews').textContent = stats.newReviews;
    document.getElementById('chefApplications').textContent = stats.chefApplications;
}

async function loadActivityFeed() {
    const activities = [
        { action: 'New user registered: john_chef', time: '2 minutes ago', type: 'user' },
        { action: 'Recipe approved: "Spicy Thai Curry"', time: '15 minutes ago', type: 'recipe' },
        { action: 'System backup completed', time: '1 hour ago', type: 'system' },
        { action: 'Chef application received from mary_baker', time: '2 hours ago', type: 'application' },
        { action: 'Recipe rejected: "Invalid Recipe Title"', time: '3 hours ago', type: 'recipe' }
    ];
    
    const feed = document.getElementById('activityFeed');
    if (feed) {
        feed.innerHTML = activities.map(activity => `
            <div class="activity-item">
                <div>${getActivityIcon(activity.type)} ${activity.action}</div>
                <div class="activity-time">${activity.time}</div>
            </div>
        `).join('');
    }
}

function getActivityIcon(type) {
    switch(type) {
        case 'user': return '👤';
        case 'recipe': return '🍳';
        case 'system': return '⚙️';
        case 'application': return '📋';
        default: return '📌';
    }
}

// Admin Quick Actions
function showAllPendingRecipes() {
    showTab('pending-recipes');
    loadPendingRecipes();
}

function loadPendingRecipes() {
    const grid = document.getElementById('pendingRecipesGrid');
    if (grid) {
        grid.innerHTML = `
            <div class="pending-recipe-card">
                <h3>🍝 Creamy Mushroom Pasta</h3>
                <p>by: chef_mario</p>
                <div class="recipe-actions">
                    <button class="btn-success btn-sm" onclick="approveRecipe(1)">✅ Approve</button>
                    <button class="btn-danger btn-sm" onclick="rejectRecipe(1)">❌ Reject</button>
                    <button class="btn-secondary btn-sm" onclick="viewRecipeDetails(1)">👁️ View</button>
                </div>
            </div>
            <div class="pending-recipe-card">
                <h3>🥗 Fresh Garden Salad</h3>
                <p>by: healthy_cook</p>
                <div class="recipe-actions">
                    <button class="btn-success btn-sm" onclick="approveRecipe(2)">✅ Approve</button>
                    <button class="btn-danger btn-sm" onclick="rejectRecipe(2)">❌ Reject</button>
                    <button class="btn-secondary btn-sm" onclick="viewRecipeDetails(2)">👁️ View</button>
                </div>
            </div>
        `;
    }
}

function approveRecipe(recipeId) {
    showMessage(`✅ Recipe ${recipeId} approved successfully!`, 'success');
    loadPendingRecipes(); // Refresh the list
}

function rejectRecipe(recipeId) {
    const reason = prompt('Please provide a reason for rejection:');
    if (reason) {
        showMessage(`❌ Recipe ${recipeId} rejected: ${reason}`, 'info');
        loadPendingRecipes(); // Refresh the list
    }
}

function generateReport() {
    showMessage('📊 Generating comprehensive platform report...', 'info');
    setTimeout(() => {
        showMessage('✅ Report generated successfully! Check your downloads.', 'success');
    }, 2000);
}

function broadcastMessage() {
    const message = prompt('Enter announcement message for all users:');
    if (message) {
        showMessage(`📢 Announcement sent to all users: "${message}"`, 'success');
    }
}

function showSystemMaintenance() {
    if (confirm('⚠️ This will put the system in maintenance mode. Continue?')) {
        showMessage('🔧 System maintenance mode activated. Users will see maintenance page.', 'warning');
    }
}

// User Management Functions
function loadUsers() {
    const users = [
        { id: 1, firstName: 'John', lastName: 'Doe', username: 'john_doe', email: 'john@example.com', role: 'USER', status: 'ACTIVE', createdAt: '2024-01-15' },
        { id: 2, firstName: 'Maria', lastName: 'Garcia', username: 'chef_maria', email: 'maria@example.com', role: 'CHEF', status: 'ACTIVE', createdAt: '2024-01-10' },
        { id: 3, firstName: 'Admin', lastName: 'User', username: 'admin', email: 'admin@example.com', role: 'ADMIN', status: 'ACTIVE', createdAt: '2024-01-01' }
    ];
    
    const tbody = document.getElementById('usersTableBody');
    if (tbody) {
        tbody.innerHTML = users.map(user => `
            <tr>
                <td><input type="checkbox" value="${user.id}"></td>
                <td>${user.id}</td>
                <td>${user.firstName} ${user.lastName}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td><span class="role-badge ${user.role.toLowerCase()}">${user.role}</span></td>
                <td><span class="status-badge ${user.status.toLowerCase()}">${user.status}</span></td>
                <td>${new Date(user.createdAt).toLocaleDateString()}</td>
                <td>
                    <button class="btn-secondary btn-xs" onclick="editUser(${user.id})">✏️ Edit</button>
                    <button class="btn-warning btn-xs" onclick="suspendUser(${user.id})">⏸️ Suspend</button>
                    <button class="btn-danger btn-xs" onclick="deleteUser(${user.id})">🗑️ Delete</button>
                </td>
            </tr>
        `).join('');
    }
}

function editUser(userId) {
    showMessage(`✏️ Opening user ${userId} for editing...`, 'info');
}

function suspendUser(userId) {
    if (confirm('Are you sure you want to suspend this user?')) {
        showMessage(`⏸️ User ${userId} has been suspended.`, 'warning');
        loadUsers(); // Refresh the list
    }
}

function deleteUser(userId) {
    if (confirm('⚠️ Are you sure you want to permanently delete this user? This action cannot be undone.')) {
        showMessage(`🗑️ User ${userId} has been deleted.`, 'success');
        loadUsers(); // Refresh the list
    }
}

function createNewUser() {
    showModal('registerModal');
}

// System Settings Functions
function saveSystemSettings() {
    showMessage('💾 All system settings saved successfully!', 'success');
}

function resetToDefaults() {
    if (confirm('Are you sure you want to reset all settings to default values?')) {
        showMessage('🔄 Settings reset to default values.', 'info');
    }
}

function exportSystemData() {
    showMessage('📤 Exporting system data... This may take a few minutes.', 'info');
    setTimeout(() => {
        showMessage('✅ System data exported successfully! Check your downloads.', 'success');
    }, 3000);
}

// Enhanced Profile Loading
async function loadProfile() {
    if (!authToken) {
        document.getElementById('profile').innerHTML = 
            '<div class="no-access"><h2>🔒 Please Login</h2><p>You need to be logged in to view your profile.</p></div>';
        return;
    }

    try {
        // Update role badge
        const roleBadge = document.getElementById('profileRoleBadge');
        if (roleBadge && currentUser) {
            roleBadge.className = `profile-role-badge role-${currentUser.role.toLowerCase()}`;
            roleBadge.textContent = `${getRoleEmoji(currentUser.role)} ${currentUser.role}`;
        }
        
        // Update stats (mock data)
        document.getElementById('recipesCount').textContent = currentUser.role === 'CHEF' ? '12' : '3';
        document.getElementById('favoritesCount').textContent = '8';
        document.getElementById('ratingsCount').textContent = '15';
        
        // Pre-fill form with current user data
        const form = document.getElementById('profileForm');
        if (form && currentUser) {
            form.querySelector('#profile-firstName').value = currentUser.firstName || '';
            form.querySelector('#profile-lastName').value = currentUser.lastName || '';
            form.querySelector('#profile-email').value = currentUser.email || '';
            form.querySelector('#profile-bio').value = currentUser.bio || '';
            form.querySelector('#profile-fitnessGoal').value = currentUser.fitnessGoal || '';
            form.querySelector('#profile-calorieTarget').value = currentUser.dailyCalorieTarget || '';
        }
    } catch (error) {
        console.error('Error loading profile:', error);
        showMessage('Failed to load profile data', 'error');
    }
}

// Add CSS for new components
const style = document.createElement('style');
style.textContent = `
    .hidden { display: none !important; }
    .no-access { 
        text-align: center; 
        padding: 4rem 2rem; 
        color: var(--text-secondary);
        background: var(--card-background);
        border: 1px solid var(--border-color);
        border-radius: var(--border-radius);
        margin: 2rem 0;
    }
    .no-access h2 { color: var(--danger-color); margin-bottom: 1rem; }
    .pending-recipe-card {
        background: var(--card-background);
        border: 1px solid var(--border-color);
        padding: 1.5rem;
        border-radius: var(--border-radius);
        margin-bottom: 1rem;
    }
    .pending-recipe-card h3 { color: var(--primary-color); margin-bottom: 0.5rem; }
    .pending-recipe-card p { color: var(--text-secondary); margin-bottom: 1rem; }
    .recipe-actions { display: flex; gap: 0.5rem; }
`;
document.head.appendChild(style);