import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './Login.css';

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username || !password) {
            setError('Preencha todos os campos.');
            return;
        }
        try {
            const response = await axios.post('http://localhost:8080/Utilizadores/login', {
                username,
                password
            });

            const token = response.data.token;
            localStorage.setItem('token', token); // guarda o JWT
            navigate('/home');
        } catch (err) {
            setError('Credenciais inválidas');
        }
    };

    return (
        <div className="login-screen">
            <div className="login-card">
                <img src="/images/img.png" alt="Logo" className="logo" />
                <img src="/images/letras.png" alt="Logo" className="logo" />
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button type="submit">LOGIN</button>
                </form>
                {error && <p style={{ color: 'yellow' }}>{error}</p>}
                <p className="register-text">
                    Ainda não tem conta? <a href="/criar-conta">Registe-se</a>
                </p>
            </div>
        </div>
    );
}

export default Login;
