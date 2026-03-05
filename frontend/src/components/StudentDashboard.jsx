import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

const StudentDashboard = () => {
    const [activeTab, setActiveTab] = useState('myProfile');
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.clear();
        navigate('/');
    };

    return (
        <div className="dashboard">
            <header>
                <div className="header-content">
                    <h1>Student Dashboard</h1>
                    <button onClick={handleLogout}>Logout</button>
                </div>
            </header>
            <nav className="tabs">
                <button onClick={() => setActiveTab('myProfile')} className={activeTab === 'myProfile' ? 'active' : ''}>My Profile</button>
                <button onClick={() => setActiveTab('myHistory')} className={activeTab === 'myHistory' ? 'active' : ''}>My History</button>
                <button onClick={() => setActiveTab('myFines')} className={activeTab === 'myFines' ? 'active' : ''}>My Fines</button>
                <button onClick={() => setActiveTab('searchBooks')} className={activeTab === 'searchBooks' ? 'active' : ''}>Search Books</button>
            </nav>
            <main className="content">
                {activeTab === 'myProfile' && <MyProfile />}
                {activeTab === 'myHistory' && <MyHistory />}
                {activeTab === 'myFines' && <MyFines />}
                {activeTab === 'searchBooks' && <SearchBooks />}
            </main>
        </div>
    );
};

const MyProfile = () => {
    const [profile, setProfile] = useState(null);

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const res = await api.get('/student/profile');
                setProfile(res.data);
            } catch (err) {
                console.error(err);
            }
        };
        fetchProfile();
    }, []);

    if (!profile) return <p>Loading profile...</p>;

    return (
        <div className="form-container">
            <h3>My Profile</h3>
            <div style={{ lineHeight: '1.8' }}>
                <p><strong>Name:</strong> {profile.name}</p>
                <p><strong>Email:</strong> {profile.email}</p>
                <p><strong>Roll No:</strong> {profile.rollNo}</p>
                <p><strong>Course:</strong> {profile.course}</p>
                <p><strong>Year:</strong> {profile.year}</p>
                <p><strong>Status:</strong> {profile.status}</p>
            </div>
        </div>
    );
};

const MyHistory = () => {
    const [history, setHistory] = useState([]);

    useEffect(() => {
        const fetchHistory = async () => {
            try {
                const res = await api.get('/student/my-history');
                setHistory(res.data);
            } catch (err) {
                console.error(err);
            }
        };
        fetchHistory();
    }, []);

    return (
        <div className="form-container">
            <h3>My Borrowing History</h3>
            {history.length === 0 ? <p>No history found.</p> : (
                <table>
                    <thead>
                        <tr>
                            <th>Book Title</th>
                            <th>Issue Date</th>
                            <th>Due Date</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {history.map((record) => (
                            <tr key={record.issueId}>
                                <td>{record.bookTitle}</td>
                                <td>{record.issueDate}</td>
                                <td>{record.dueDate}</td>
                                <td>{record.status}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

const MyFines = () => {
    const [fines, setFines] = useState([]);

    useEffect(() => {
        const fetchFines = async () => {
            try {
                const res = await api.get('/student/my-fines');
                setFines(res.data);
            } catch (err) {
                console.error(err);
            }
        };
        fetchFines();
    }, []);

    return (
        <div className="form-container">
            <h3>My Fines</h3>
            {fines.length === 0 ? <p>No fines found.</p> : (
                <table>
                    <thead>
                        <tr>
                            <th>Book</th>
                            <th>Amount</th>
                            <th>Date</th>
                            <th>Paid</th>
                        </tr>
                    </thead>
                    <tbody>
                        {fines.map((fine) => (
                            <tr key={fine.id}>
                                <td>{fine.bookTitle || 'N/A'}</td>
                                <td style={{ color: 'red', fontWeight: 'bold' }}>${fine.amount}</td>
                                <td>{new Date(fine.fineDate).toLocaleDateString()}</td>
                                <td>{fine.paid ? 'Yes' : 'No'}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

const SearchBooks = () => {
    const [query, setQuery] = useState('');
    const [searchType, setSearchType] = useState('title');
    const [books, setBooks] = useState([]);

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            let endpoint = '';
            if (searchType === 'title') {
                endpoint = `/books/search/title?title=${query}`;
            } else {
                endpoint = `/books/search/author?author=${query}`;
            }

            const res = await api.get(endpoint);
            setBooks(res.data);
        } catch (err) {
            console.error(err);
            setBooks([]);
        }
    };

    return (
        <div className="form-container">
            <h3>Search Books</h3>
            <form onSubmit={handleSearch} className="search-form" style={{ flexDirection: 'column', alignItems: 'flex-start' }}>
                <div style={{ marginBottom: '10px' }}>
                    <label style={{ marginRight: '15px' }}>
                        <input
                            type="radio"
                            name="searchType"
                            value="title"
                            checked={searchType === 'title'}
                            onChange={() => setSearchType('title')}
                            style={{ width: 'auto', marginBottom: 0 }}
                        /> Search by Title
                    </label>
                    <label>
                        <input
                            type="radio"
                            name="searchType"
                            value="author"
                            checked={searchType === 'author'}
                            onChange={() => setSearchType('author')}
                            style={{ width: 'auto', marginBottom: 0 }}
                        /> Search by Author
                    </label>
                </div>
                <div style={{ display: 'flex', gap: '10px', width: '100%' }}>
                    <input
                        placeholder={`Enter ${searchType === 'title' ? 'Book Title' : 'Author Name'}`}
                        value={query}
                        onChange={e => setQuery(e.target.value)}
                    />
                    <button type="submit" style={{ width: 'auto' }}>Search</button>
                </div>
            </form>
            <div className="book-list">
                {books.length === 0 && <p>No books found.</p>}
                {books.map(book => (
                    <div key={book.id || book.bookId || Math.random()} className="book-card">
                        <h4>{book.title}</h4>
                        <p>Author: {book.author}</p>
                        <p>Category: {book.categoryName}</p>
                        <p>Quantity: {book.quantity}</p>
                        <p>ISBN: {book.isbn}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default StudentDashboard;
