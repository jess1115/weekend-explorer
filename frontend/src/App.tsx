import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Home from './pages/Home';
import Explore from './pages/Explore';
import Recommend from './pages/Recommend';
import Saved from './pages/Saved';
import Footer from "./components/Footer.tsx";
import ActivityDetail from "./pages/ActivityDetail.tsx";

function App() {
  return (
      <>
          <Navbar />

          <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/explore" element={<Explore />} />
              <Route path="/recommend" element={<Recommend />} />
              <Route path="/saved" element={<Saved />} />
              <Route path="/activities/:id" element={<ActivityDetail />} />
          </Routes>

          <Footer />
      </>
  );
}

export default App;