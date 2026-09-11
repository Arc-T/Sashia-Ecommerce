import { Link } from "@tanstack/react-router";

export default function NotFoundComponent() {
  return (
    <div>
      <p>the fuck you think you're going ha?</p>
      <Link to="/login">Login</Link>
    </div>
  );
}
