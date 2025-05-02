import React, { useState } from 'react';
import './Login.css';

function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Username:', username);
        console.log('Password:', password);
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
                <p className="register-text">
                    Ainda não tem conta? <a href="/criar-conta">Registe-se</a>
                </p>
            </div>
        </div>
    );
}

export default Login;
