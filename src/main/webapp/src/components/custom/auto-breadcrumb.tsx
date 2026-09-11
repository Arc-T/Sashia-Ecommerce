import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Fragment } from "react";
import { useTranslation } from "react-i18next";
import { Link, useMatches } from "react-router";

interface RouteBreadcrumbContext {
  breadcrumb: string;
}

interface BreadcrumbEntry {
  key: string;
  path: string;
  labelKey: string;
}

const isValidBreadcrumbContext = (
  context: unknown,
): context is RouteBreadcrumbContext =>
  typeof context === "object" &&
  context !== null &&
  typeof (context as Record<string, unknown>).breadcrumb === "string";

function toBreadcrumbEntry(
  context: RouteBreadcrumbContext,
  pathname: string,
): BreadcrumbEntry {
  return {
    key: pathname,
    path: pathname,
    labelKey: context.breadcrumb,
  };
}

export default function AutoBreadcrumb() {
  const matches = useMatches();
  const { t } = useTranslation();

  const crumbs: BreadcrumbEntry[] = [];

  for (const match of matches) {
    if (isValidBreadcrumbContext(match.context)) {
      crumbs.push(toBreadcrumbEntry(match.context, match.pathname));
    }
  }

  // Bail early – no DOM nodes created if there's nothing to show
  if (crumbs.length === 0) return null;

  const lastIndex = crumbs.length - 1;

  return (
    <Breadcrumb aria-label="Breadcrumb">
      <BreadcrumbList>
        {crumbs.map(({ key, path, labelKey }, index) => (
          <Fragment key={key}>
            <BreadcrumbItem>
              {index === lastIndex ? (
                <BreadcrumbPage>{t(labelKey)}</BreadcrumbPage>
              ) : (
                <BreadcrumbLink asChild>
                  <Link to={path}>{t(labelKey)}</Link>
                </BreadcrumbLink>
              )}
            </BreadcrumbItem>

            {index !== lastIndex && <BreadcrumbSeparator />}
          </Fragment>
        ))}
      </BreadcrumbList>
    </Breadcrumb>
  );
}
