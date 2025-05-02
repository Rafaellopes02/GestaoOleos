import React, { useState } from 'react';
import './CriarConta.css';

function CriarConta() {
    const [name, setName] = useState('');
    const [phone, setPhone] = useState('');
    const [street, setStreet] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [password1, setPassword1] = useState('');


    const handleSubmit = (e) => {
        e.preventDefault();
        console.log('Username:', username);
        console.log('Password:', password);
    };

    return (
        <div className="criarconta-screen">
            <div className="criarconta-card">
                <img src="/images/img.png" alt="Logo" className="logo" />
                <img src="/images/letras.png" alt="Logo" className="logo" />
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Nome"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />
                    <input
                        type="text"
                        placeholder="Telefone"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                    />
                    <input
                        type="text"
                        placeholder="Morada"
                        value={street}
                        onChange={(e) => setStreet(e.target.value)}
                    />
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
                    <input
                        type="password1"
                        placeholder="Repita a Password"
                        value={password1}
                        onChange={(e) => setPassword1(e.target.value)}
                    />
                    <button type="submit">CRIAR CONTA</button>
                </form>
                <p className="login-text">
                    Já tem conta? <a href="/">Faça Login</a>
                </p>
            </div>
        </div>
    );
}

export default CriarConta;
